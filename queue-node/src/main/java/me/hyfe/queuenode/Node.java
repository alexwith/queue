package me.hyfe.queuenode;

import me.hyfe.helper.Events;
import me.hyfe.helper.plugin.HelperPlugin;
import me.hyfe.queuenode.commands.QueueAdminCommand;
import me.hyfe.queuenode.commands.QueueCommand;
import me.hyfe.queuenode.configs.ConfigKeys;
import me.hyfe.queuenode.configs.LangKeys;
import me.hyfe.queuenode.configs.RedisKeys;
import me.hyfe.queuenode.queue.QueueManager;
import me.hyfe.queuenode.redis.Credentials;
import me.hyfe.queuenode.redis.Redis;
import me.hyfe.queuenode.tasks.StatusTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Optional;

public class Node extends HelperPlugin {
    private Redis redis;
    private QueueManager queueManager;

    private Optional<StatusTask> statusTask;

    @Override
    protected void enable() {
        this.setupConfigs();

        this.redis = Redis.createInstance(Credentials.fromRedisKeys());
        this.queueManager = new QueueManager(this);

        this.statusTask = StatusTask.tryStart(this);

        this.registerCommands();
        this.registerChannels();
        this.registerListeners();
    }

    @Override
    protected void disable() {
        this.statusTask.ifPresent(StatusTask::close);
        this.queueManager.clearQueues();
        this.redis.close();
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
        new QueueAdminCommand(this);
    }

    private void registerChannels() {
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }

    private void registerListeners() {
        Events.subscribe(PlayerQuitEvent.class).handler((event) -> {
            Player player = event.getPlayer();
            this.queueManager.dequeue(player);
        });
    }
}
