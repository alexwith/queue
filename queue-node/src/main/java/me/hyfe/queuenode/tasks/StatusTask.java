package me.hyfe.queuenode.tasks;

import com.google.gson.JsonObject;
import me.hyfe.helper.Schedulers;
import me.hyfe.helper.scheduler.Task;
import me.hyfe.helper.terminable.Terminable;
import me.hyfe.queuenode.Node;
import me.hyfe.queuenode.configs.ConfigKeys;
import me.hyfe.queuenode.redis.Redis;
import org.bukkit.Server;

import java.util.Optional;

public class StatusTask implements Runnable, Terminable {
    private final Redis redis;
    private final Server server;
    private final Task handle;

    private final String key = ConfigKeys.NODE_IDENTIFIER.get().concat("-status").toLowerCase();

    private static final int TICK_INTERVAL = 100; // 5 seconds

    public StatusTask(Node node) {
        this.redis = node.getRedis();
        this.server = node.getServer();
        this.handle = Schedulers.async().runRepeating(this, 0, TICK_INTERVAL);
    }

    public static Optional<StatusTask> tryStart(Node node) {
        return ConfigKeys.IS_HUB.get() ? Optional.empty() : Optional.of(new StatusTask(node));
    }

    @Override
    public void run() {
        this.redis.provideJedis((jedis) -> {
            jedis.set(this.key, this.generateStatus());
        });
    }

    @Override
    public void close() {
        this.handle.close();
        this.redis.provideJedis((jedis) -> {
            jedis.del(this.key);
        });
    }

    private String generateStatus() {
        JsonObject status = new JsonObject();
        status.addProperty("online-players", this.server.getOnlinePlayers().size());
        status.addProperty("max-players", this.server.getMaxPlayers());
        status.addProperty("whitelisted", this.server.hasWhitelist());
        return status.toString();
    }
}
