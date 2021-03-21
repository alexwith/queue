package me.hyfe.queue;

import me.hyfe.queue.bootstrap.Bootstrap;
import me.hyfe.queue.bootstrap.BootstrapProvider;
import me.hyfe.queue.priorities.PriorityManager;
import me.hyfe.queue.proxy.delegates.ProxyDelegate;
import me.hyfe.queue.proxy.delegates.ProxyMessageDelegate;
import me.hyfe.queue.queue.QueueManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class QueuePlugin extends Plugin implements BootstrapProvider<ProxiedPlayer, ServerInfo> {
    private Bootstrap bootstrap;

    private final ProxyDelegate<ProxiedPlayer> proxyDelegate = ProxyServer.getInstance()::getPlayer;
    private final ProxyMessageDelegate<ProxiedPlayer> messageDelegate = (player, message) -> player.sendMessage(new TextComponent(message));

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
    public @NotNull QueueManager<ProxiedPlayer, ServerInfo> createQueueManager() {
        return new BungeeQueueManager(this);
    }

    @Override
    public @NotNull PriorityManager<ProxiedPlayer> createPriorityManager() {
        return new PriorityManager<>(CommandSender::hasPermission);
    }

    @Override
    public @NotNull ProxyDelegate<ProxiedPlayer> getProxyDelegate() {
        return this.proxyDelegate;
    }

    @Override
    public @NotNull ProxyMessageDelegate<ProxiedPlayer> getMessageDelegate() {
        return this.messageDelegate;
    }

    @Override
    public void registerListeners() {
        this.getProxy().getPluginManager().registerListener(this, new BungeeConnectionListener());
    }
}
