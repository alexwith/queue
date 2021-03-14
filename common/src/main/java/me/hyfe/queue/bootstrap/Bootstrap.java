package me.hyfe.queue.bootstrap;

import me.hyfe.queue.config.ConfigController;
import me.hyfe.queue.config.keys.ConfigKeys;
import me.hyfe.queue.config.keys.LangKeys;
import me.hyfe.queue.config.keys.RedisKeys;
import me.hyfe.queue.queue.QueueManager;
import me.hyfe.queue.redis.Credentials;
import me.hyfe.queue.redis.Redis;
import me.hyfe.queue.redis.RedisProvider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Bootstrap implements RedisProvider {
    private final ConfigController configController;
    private final Redis redis;
    private final QueueManager<?, ?> queueManager;

    private static Bootstrap instance;

    public static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(10);
    public static final ScheduledExecutorService SCHEDULED_EXECUTOR = Executors.newScheduledThreadPool(10);

    public Bootstrap(BootstrapProvider<?, ?> parent) {
        instance = this;
        this.configController = new ConfigController();
        this.configController.registerConfigs(
                new RedisKeys(),
                new ConfigKeys(),
                new LangKeys()
        );
        this.redis = Redis.createInstance(Credentials.fromRedisKeys());
        this.queueManager = parent.createQueueManager();
        parent.registerListeners();
    }

    public static Bootstrap create(BootstrapProvider<?, ?> parent) {
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
    public <T extends QueueManager<?, ?>> T getQueueManager() {
        return (T) this.queueManager;
    }

    public void terminate() {
        this.queueManager.clearQueues();
        this.redis.close();
    }
}
