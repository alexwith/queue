package me.hyfe.queue.bootstrap;

import me.hyfe.queue.config.ConfigController;
import me.hyfe.queue.config.holders.RedisKeys;
import me.hyfe.queue.queue.QueueManager;
import me.hyfe.queue.redis.Credentials;
import me.hyfe.queue.redis.Redis;
import me.hyfe.queue.redis.RedisProvider;
import org.jetbrains.annotations.NotNull;

public class Bootstrap implements RedisProvider {
    private final ConfigController configController;
    private final Redis redis;
    private final QueueManager<?> queueManager;

    private static Bootstrap instance;

    public Bootstrap(BootstrapProvider<?> parent) {
        instance = this;
        this.configController = new ConfigController();
        this.configController.registerConfigs(
                new RedisKeys()
        );
        parent.registerListeners();
        this.redis = Redis.createInstance(Credentials.fromRedisKeys());
        this.queueManager = parent.createQueueManager();
    }

    public static Bootstrap create(BootstrapProvider<?> parent) {
        return new Bootstrap(parent);
    }

    public static Bootstrap get() {
        return instance;
    }

    public ConfigController getConfigController() {
        return this.configController;
    }

    @Override
    public @NotNull Redis getRedis() {
        return this.redis;
    }

    @SuppressWarnings("unchecked")
    public <T extends QueueManager<?>> T getQueueManager() {
        return (T) this.queueManager;
    }

    public void terminate() {
        this.redis.close();
    }
}
