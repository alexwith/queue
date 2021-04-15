package me.hyfe.queuenode.queue;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.hyfe.helper.Schedulers;
import me.hyfe.helper.config.ConfigController;
import me.hyfe.helper.promise.Promise;
import me.hyfe.queuenode.Node;
import me.hyfe.queuenode.configs.ConfigKeys;
import me.hyfe.queuenode.configs.LangKeys;
import me.hyfe.queuenode.priority.PriorityManager;
import me.hyfe.queuenode.tasks.PositionTask;
import me.hyfe.queuenode.tasks.QueueTask;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class QueueManager {
    private final Node node;
    private final Optional<PositionTask> positionTask;
    private final PriorityManager priorityManager;

    private final Map<String, Queue> map = new ConcurrentHashMap<>();
    private final Set<QueueTask> queueTasks = new HashSet<>();

    public QueueManager(Node node) {
        this.node = node;
        this.positionTask = PositionTask.tryStart(this);
        this.priorityManager = new PriorityManager();
    }

    public Queue createQueue(String key, String server) {
        return new Queue(this.node, key, server);
    }

    public void loadPriorities(ConfigController configController) {
        this.priorityManager.load(configController);
    }

    public boolean isOnline(String server) {
        return this.node.getRedis().provideJedis((jedis) -> {
            return jedis.exists(server.concat("-queue"));
        });
    }

    public boolean isWhitelisted(String server) {
        JsonParser parser = new JsonParser();
        return this.node.getRedis().provideJedis((jedis) -> {
            String key = server.concat("-queue");
            if (jedis.exists(key)) {
                return false;
            }
            String jsonString = jedis.get(key);
            JsonObject json = parser.parse(jsonString).getAsJsonObject();
            return json.get("whitelisted").getAsBoolean();
        });
    }

    public Collection<Queue> getQueues() {
        return this.map.values();
    }

    public Queue getQueue(String server) {
        return this.map.get(server.concat("-queue").toLowerCase());
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

    public void clearQueues() {
        Schedulers.async().run(() -> {
            this.positionTask.ifPresent(PositionTask::close);
            for (QueueTask task : this.queueTasks) {
                task.close();
            }
            for (Queue queue : this.map.values()) {
                queue.clear();
            }
        });
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

    public Promise<Void> queue(Player player, String server) {
        return Schedulers.async().run(() -> {
            String key = server.concat("-queue").toLowerCase();
            UUID uuid = player.getUniqueId();
            if (ConfigKeys.NON_HUB_NODES.get().contains(server) && !this.map.containsKey(key)) {
                Queue queue = this.createQueue(key, server);
                this.map.put(key, queue);
                this.queueTasks.add(new QueueTask(this, queue));
            }
            if (!this.map.containsKey(key)) {
                if (ConfigKeys.NON_HUB_NODES.get().contains(server)) {
                    Queue queue = this.createQueue(key, server);
                    this.map.put(key, queue);
                    this.queueTasks.add(new QueueTask(this, queue));
                } else {
                    LangKeys.QUEUE_NOT_FOUND.send(player);
                    return;
                }
            }
            Queue queue = this.getInQueue(uuid).join();
            if (queue != null) {
                LangKeys.ALREADY_QUEUED.send(player, replacer -> replacer
                        .set("server", queue.getServer())
                );
                return;
            }
            LangKeys.JOINED_QUEUE.send(player, replacer -> replacer
                    .set("server", server)
            );
            QueuePlayer proxyPlayer = QueuePlayer.of(uuid, this.priorityManager.getPriority(player));
            this.map.get(key).queue(proxyPlayer);
            this.sendPosition(player, uuid);
        });
    }

    public Promise<Void> dequeue(Player player) {
        return Schedulers.async().run(() -> {
            UUID uuid = player.getUniqueId();
            Queue queue = this.getInQueue(uuid).join();
            if (queue == null) {
                return;
            }
            QueuePlayer proxyPlayer = QueuePlayer.of(uuid, this.priorityManager.getPriority(player));
            queue.dequeue(proxyPlayer);
        });
    }

    public void sendToServer(Player player, String server) {
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            DataOutputStream stream = new DataOutputStream(byteStream);
            stream.writeUTF("Connect");
            stream.writeUTF(server);
            player.sendPluginMessage(this.node, "BungeeCord", byteStream.toByteArray());
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }
}
