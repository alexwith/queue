package me.hyfe.queue;

import me.hyfe.queue.queue.Queue;
import me.hyfe.queue.queue.QueueManager;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BungeeQueueManager extends QueueManager<ProxiedPlayer, ServerInfo> {
    private final QueuePlugin plugin;

    public BungeeQueueManager(QueuePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Queue<ProxiedPlayer, ServerInfo> createQueue(String key, ServerInfo target) {
        return new Queue<>(this.plugin.bootstrap(), key, this.plugin.getProxyDelegate(), new BungeeServerSender(target));
    }
}
