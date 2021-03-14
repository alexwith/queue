package me.hyfe.queue.queue;

import me.hyfe.queue.bootstrap.BootstrapProvider;
import me.hyfe.queue.config.keys.LangKeys;
import me.hyfe.queue.proxy.ProxyMessageDelegate;
import me.hyfe.queue.proxy.QueueProxyPlayer;
import me.hyfe.queue.queue.tasks.QueueTask;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class QueueManager<T, U> {
    private final Map<String, Queue<T, U>> map = new ConcurrentHashMap<>();
    private final Map<String, QueueTask<T, U>> tasks = new ConcurrentHashMap<>();
    private final Set<T> inTransit = new HashSet<>();
    private final ProxyMessageDelegate<T> messageDelegate;

    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();

    public QueueManager(BootstrapProvider<T, U> bootstrapProvider) {
        this.messageDelegate = bootstrapProvider.getMessageDelegate();
    }

    public abstract Queue<T, U> createQueue(String key, String server, U target);

    public ProxyMessageDelegate<T> getMessageDelegate() {
        return this.messageDelegate;
    }

    public Collection<Queue<T, U>> getQueues() {
        return this.map.values();
    }

    public void sendPosition(T player, UUID uuid) {
        CompletableFuture.runAsync(() -> {
            Queue<T, U> queue = this.getInQueue(uuid).join();
            QueueProxyPlayer<T> proxyPlayer = QueueProxyPlayer.of(player, uuid);
            LangKeys.QUEUE_POSITION.send(player, this.messageDelegate, replacer -> replacer
                    .set("position", queue.indexOf(proxyPlayer) + 1)
                    .set("total", queue.length())
                    .set("server", queue.getServer())
            );
        }, EXECUTOR);
    }

    public boolean isIntTransit(T player) {
        return this.inTransit.contains(player);
    }

    public void callTransit(T player, boolean complete) {
        if (complete) {
            this.inTransit.remove(player);
        } else {
            this.inTransit.add(player);
        }
    }

    public CompletableFuture<Void> clearQueues() {
        return CompletableFuture.runAsync(() -> {
            for (QueueTask<?, ?> task : this.tasks.values()) {
                task.terminate();
            }
            for (Queue<?, ?> queue : this.map.values()) {
                queue.clear();
            }
        });
    }

    public Queue<T, U> getQueue(String server) {
        String key = server.concat("-queue");
        return this.map.get(key);
    }

    public CompletableFuture<Boolean> isInQueue(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            return this.getInQueue(uuid).join() != null;
        });
    }

    public CompletableFuture<Queue<T, U>> getInQueue(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            for (Queue<T, U> queue : this.map.values()) {
                for (int i = 0; i < queue.length(); i++) {
                    if (queue.get(i).getUuid().equals(uuid)) {
                        return queue;
                    }
                }
            }
            return null;
        });
    }

    public CompletableFuture<Void> queue(T player, UUID uuid, String server, U target) {
        return CompletableFuture.runAsync(() -> {
            String key = server.concat("-queue");
            if (!this.map.containsKey(key)) {
                Queue<T, U> queue = this.createQueue(key, server, target);
                this.map.put(key, queue);
                this.tasks.put(key, new QueueTask<>(this, queue));
            }
            LangKeys.JOINED_QUEUE.send(player, this.messageDelegate, replacer -> replacer
                    .set("server", server)
            );
            QueueProxyPlayer<T> proxyPlayer = QueueProxyPlayer.of(player, uuid);
            Queue<T, U> queue = this.map.get(key);
            queue.queue(proxyPlayer);
            this.sendPosition(player, uuid);
        });
    }

    public CompletableFuture<Void> dequeue(T player, UUID uuid) {
        return CompletableFuture.runAsync(() -> {
            Queue<T, U> queue = this.getInQueue(uuid).join();
            if (queue == null) {
                return;
            }
            QueueProxyPlayer<T> proxyPlayer = QueueProxyPlayer.of(player, uuid);
            queue.dequeue(proxyPlayer);
        });
    }
}
