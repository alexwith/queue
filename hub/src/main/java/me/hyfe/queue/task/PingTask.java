package me.hyfe.queue.task;

import me.hyfe.helper.Schedulers;
import me.hyfe.helper.scheduler.Task;
import me.hyfe.helper.terminable.Terminable;
import me.hyfe.queue.HubPlugin;
import me.hyfe.queue.configs.ConfigKeys;
import me.hyfe.queue.managers.ServerManager;
import me.hyfe.queue.objects.Server;
import me.hyfe.queue.redis.Redis;

public class PingTask implements Runnable, Terminable {
    private final ServerManager serverManager;
    private final Redis redis;
    private final Task task;

    public PingTask(HubPlugin plugin) {
        this.serverManager = plugin.getServerManager();
        this.redis = plugin.getRedis();
        this.task = Schedulers.async().runRepeating(this, 0, ConfigKeys.PING_INTERVAL.get());
    }

    @Override
    public void run() {
        for (Server server : this.serverManager.getServers()) {
            server.ping(this.redis);
        }
    }

    @Override
    public void close() {
        this.task.close();
    }
}
