package me.hyfe.queuenode;

import me.hyfe.helper.plugin.HelperPlugin;
import me.hyfe.queuenode.commands.QueueCommand;
import me.hyfe.queuenode.configs.ConfigKeys;
import me.hyfe.queuenode.configs.LangKeys;
import me.hyfe.queuenode.configs.RedisKeys;
import me.hyfe.queuenode.queue.QueueManager;
import me.hyfe.queuenode.redis.Credentials;
import me.hyfe.queuenode.redis.Redis;
import me.hyfe.queuenode.tasks.PositionTask;
import me.hyfe.queuenode.tasks.StatusTask;

import java.util.Optional;

public class Node extends HelperPlugin {
    private Redis redis;
    private QueueManager queueManager;

    private Optional<StatusTask> statusTask;
    private Optional<PositionTask> positionTask;

    @Override
    protected void enable() {
        this.setupConfigs();

        this.redis = Redis.createInstance(Credentials.fromRedisKeys());
        this.queueManager = new QueueManager(this);

        this.statusTask = StatusTask.tryStart(this);
        this.positionTask = PositionTask.tryStart(this);

        this.registerCommands();
    }

    @Override
    protected void disable() {
        this.statusTask.ifPresent(StatusTask::close);
        this.positionTask.ifPresent(PositionTask::close);
    }

    public Redis getRedis() {
        return this.redis;
    }

    public QueueManager getQueueManager() {
        return this.queueManager;
    }

    private void setupConfigs() {
        this.configController.registerConfigs(
                new ConfigKeys(),
                new LangKeys(),
                new RedisKeys()
        );
    }

    private void registerCommands() {
        new QueueCommand(this);
    }
}
