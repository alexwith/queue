package me.hyfe.queue.queue;

import me.hyfe.queue.proxy.QueueProxyPlayer;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public abstract class QueueManager<T, U> {
    private final Map<String, Queue<T, U>> map = new ConcurrentHashMap<>();
    private final Map<String, QueueTask<T, U>> tasks = new ConcurrentHashMap<>();

    public abstract Queue<T, U> createQueue(String key, U target);

    public Queue<T, U> getQueue(String server) {
        String key = server.concat("-queue");
        return this.map.get(key);
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

    public CompletableFuture<Boolean> isInQueue(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            for (Queue<T, U> queue : this.map.values()) {
                for (int i = 0; i < queue.length(); i++) {
                    if (queue.get(i).getUuid().equals(uuid)) {
                        return true;
                    }
                }
            }
            return false;
        });
    }

    public CompletableFuture<Void> queue(T player, UUID uuid, String server, U target) {
        return CompletableFuture.runAsync(() -> {
            String key = server.concat("-queue");
            if (!this.map.containsKey(key)) {
                Queue<T, U> queue = this.createQueue(key, target);
                this.map.put(key, queue);
                this.tasks.put(key, new QueueTask<>(queue));
            }
            QueueProxyPlayer<T> proxyPlayer = QueueProxyPlayer.of(player, uuid);
            Queue<T, U> queue = this.map.get(key);
            queue.queue(proxyPlayer);
        });
    }
}
