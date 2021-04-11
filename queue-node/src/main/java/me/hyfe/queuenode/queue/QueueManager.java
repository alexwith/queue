package me.hyfe.queuenode.queue;

import me.hyfe.helper.Schedulers;
import me.hyfe.helper.config.ConfigController;
import me.hyfe.helper.promise.Promise;
import me.hyfe.queuenode.Node;
import me.hyfe.queuenode.configs.LangKeys;
import me.hyfe.queuenode.priority.PriorityManager;
import me.hyfe.queuenode.tasks.PositionTask;
import me.hyfe.queuenode.tasks.QueueTask;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class QueueManager {
    private final Node node;
    private final PositionTask positionTask;
    private final PriorityManager priorityManager;

    private final Map<String, Queue> map = new ConcurrentHashMap<>();
    private final Set<QueueTask> queueTasks = new HashSet<>();
    private final Set<UUID> inTransit = new HashSet<>();

    public QueueManager(Node node) {
        this.node = node;
        this.positionTask = new PositionTask(this.node);
        this.priorityManager = new PriorityManager();
    }

    public Queue createQueue(String key, String server) {
        return new Queue(this.node, key, server);
    }

    public void loadPriorities(ConfigController configController) {
        this.priorityManager.load(configController);
    }

    public boolean isOnline(String server) {
        return false;
    }

    public Collection<Queue> getQueues() {
        return this.map.values();
    }

    public Promise<Void> sendPosition(Player player, UUID uuid) {
        return Schedulers.async().run(() -> {
            Queue queue = this.getInQueue(uuid).join();
            QueuePlayer proxyPlayer = QueuePlayer.of(uuid, this.priorityManager.getPriority(player));
            LangKeys.QUEUE_POSITION.send(player, replacer -> replacer
                    .set("position", queue.indexOf(proxyPlayer) + 1)
                    .set("total", queue.length())
                    .set("server", queue.getServer())
            );
        });
    }

    public boolean isIntTransit(UUID uuid) {
        return this.inTransit.contains(uuid);
    }

    public void callTransit(UUID uuid, boolean complete) {
        if (complete) {
            this.inTransit.remove(uuid);
        } else {
            this.inTransit.add(uuid);
        }
    }

    public Promise<Void> clearQueues() {
        return Schedulers.async().run(() -> {
            this.positionTask.close();
            for (QueueTask task : this.queueTasks) {
                task.close();
            }
            for (Queue queue : this.map.values()) {
                queue.clear();
            }
        });
    }

    public Queue getQueue(String server) {
        String key = server.concat("-queue");
        return this.map.get(key);
    }

    public Promise<Boolean> isInQueue(UUID uuid) {
        return Schedulers.async().supply(() -> this.getInQueue(uuid).join() != null);
    }

    public Promise<Queue> getInQueue(UUID uuid) {
        return Schedulers.async().supply(() -> {
            for (Queue queue : this.map.values()) {
                for (int i = 0; i < queue.length(); i++) {
                    if (queue.get(i).getUuid().equals(uuid)) {
                        return queue;
                    }
                }
            }
            return null;
        });
    }

    public Promise<Void> queue(Player player, UUID uuid, String server) {
        return Schedulers.async().run(() -> {
            String key = server.concat("-queue");
            if (!this.map.containsKey(key)) {
                Queue queue = this.createQueue(key, server);
                this.map.put(key, queue);
                this.queueTasks.add(new QueueTask(this, queue));
            }
            LangKeys.JOINED_QUEUE.send(player, replacer -> replacer
                    .set("server", server)
            );
            QueuePlayer proxyPlayer = QueuePlayer.of(uuid, this.priorityManager.getPriority(player));
            Queue queue = this.map.get(key);
            queue.queue(proxyPlayer);
            this.sendPosition(player, uuid);
        });
    }

    public Promise<Void> dequeue(Player player, UUID uuid) {
        return Schedulers.async().run(() -> {
            Queue queue = this.getInQueue(uuid).join();
            if (queue == null) {
                return;
            }
            QueuePlayer proxyPlayer = QueuePlayer.of(uuid, this.priorityManager.getPriority(player));
            queue.dequeue(proxyPlayer);
        });
    }
}
