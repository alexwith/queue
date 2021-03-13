package me.hyfe.queue;

import me.hyfe.queue.queue.Queue;
import me.hyfe.queue.queue.QueueManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BungeeQueueManager extends QueueManager<ProxiedPlayer> {
    private final QueuePlugin plugin;

    public BungeeQueueManager(QueuePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Queue<ProxiedPlayer> createQueue(String key) {
        return new Queue<>(this.plugin.bootstrap(), key, this.plugin.getProxyDelegate());
    }
}
