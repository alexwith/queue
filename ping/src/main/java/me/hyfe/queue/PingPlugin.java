package me.hyfe.queue;

import me.hyfe.helper.Schedulers;
import me.hyfe.helper.plugin.HelperPlugin;
import me.hyfe.helper.scheduler.Task;
import me.hyfe.queue.config.RedisKeys;
import me.hyfe.queue.redis.Credentials;
import me.hyfe.queue.redis.Redis;
import org.bukkit.Bukkit;
import org.bukkit.Server;

import java.util.concurrent.TimeUnit;

public class PingPlugin extends HelperPlugin {
    private Server server;
    private Redis redis;
    private String id;
    private Task pingTask;

    @Override
    protected void enable() {
        this.configController.registerConfigs(
                new RedisKeys()
        );
        this.server = Bukkit.getServer();
        this.redis = Redis.createInstance(Credentials.fromRedisKeys());
        this.id = this.server.getIp() + ":" + this.server.getPort();
        this.pingTask = Schedulers.async().runRepeating(this.getPingTask(), 0, TimeUnit.SECONDS, 1, TimeUnit.MINUTES);
    }

    @Override
    protected void disable() {
        this.pingTask.close();
        this.redis.provideJedis((jedis) -> {
            jedis.del(this.id);
        });
    }

    private Runnable getPingTask() {
        return () -> {
            this.redis.provideJedis((jedis) -> {
                jedis.set(this.id, "{" +
                        "\"players\": {" +
                        "\"max\": " + this.server.getMaxPlayers() + "," +
                        "\"online\": " + this.server.getOnlinePlayers().size() +
                        "}" +
                        "}"
                );
            });
        };
    }
}
