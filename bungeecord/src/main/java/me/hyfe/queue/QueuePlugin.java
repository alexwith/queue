package me.hyfe.queue;

import me.hyfe.queue.bootstrap.Bootstrap;
import me.hyfe.queue.bootstrap.BootstrapProvider;
import me.hyfe.queue.listeners.ConnectionListener;
import me.hyfe.queue.proxy.ProxyDelegate;
import me.hyfe.queue.queue.QueueManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class QueuePlugin extends Plugin implements BootstrapProvider<ProxiedPlayer> {
    private Bootstrap bootstrap;

    private final ProxyDelegate<ProxiedPlayer> proxyDelegate = ProxyServer.getInstance()::getPlayer;

    @Override
    public void onEnable() {
        this.bootstrap = Bootstrap.create(this);
    }

    @Override
    public void onDisable() {
        this.bootstrap.terminate();
    }

    @Override
    public @NotNull Bootstrap bootstrap() {
        return this.bootstrap;
    }

    @Override
    public @NotNull QueueManager<ProxiedPlayer> createQueueManager() {
        return new BungeeQueueManager(this);
    }

    @Override
    public @NotNull ProxyDelegate<ProxiedPlayer> getProxyDelegate() {
        return this.proxyDelegate;
    }

    @Override
    public void registerListeners() {
        this.getProxy().getPluginManager().registerListener(this, new ConnectionListener());
    }
}
