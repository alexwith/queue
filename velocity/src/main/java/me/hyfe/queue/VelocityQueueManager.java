package me.hyfe.queue;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import me.hyfe.queue.queue.Queue;
import me.hyfe.queue.queue.QueueManager;

import java.net.SocketAddress;

public class VelocityQueueManager extends QueueManager<Player, RegisteredServer> {
    private final QueuePlugin plugin;

    public VelocityQueueManager(QueuePlugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    public Queue<Player, RegisteredServer> createQueue(String key, String server, RegisteredServer target) {
        return new Queue<>(this.plugin.bootstrap(), key, server, this.plugin.getProxyDelegate(), new VelocityServerSender(target));
    }

    @Override
    public SocketAddress getSocketAddress(String server) {
        return this.plugin.getProxy().getServer(server).get().getServerInfo().getAddress();
    }
}
