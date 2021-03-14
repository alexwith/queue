package me.hyfe.queue;

import me.hyfe.queue.queue.Queue;
import me.hyfe.queue.queue.QueueManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.net.SocketAddress;

public class BungeeQueueManager extends QueueManager<ProxiedPlayer, ServerInfo> {
    private final QueuePlugin plugin;

    public BungeeQueueManager(QueuePlugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    public Queue<ProxiedPlayer, ServerInfo> createQueue(String key, String server, ServerInfo target) {
        return new Queue<>(this.plugin.bootstrap(), key, server, this.plugin.getProxyDelegate(), new BungeeServerSender(target));
    }

    @Override
    public SocketAddress getSocketAddress(String server) {
        return ProxyServer.getInstance().getServers().get(server).getSocketAddress();
    }
}
