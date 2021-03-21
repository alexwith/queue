package me.hyfe.queue;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import me.hyfe.queue.bootstrap.Bootstrap;
import me.hyfe.queue.bootstrap.BootstrapProvider;
import me.hyfe.queue.priorities.PriorityManager;
import me.hyfe.queue.proxy.delegates.ProxyDelegate;
import me.hyfe.queue.proxy.delegates.ProxyMessageDelegate;
import me.hyfe.queue.queue.QueueManager;
import net.kyori.text.TextComponent;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

@Plugin(id = "queue", name = "Queue", version = "1.0.0", description = "Queue plugin", authors = {"hyfe"})
public class QueuePlugin implements BootstrapProvider<Player, RegisteredServer> {

    private Bootstrap bootstrap;

    private final ProxyServer proxy;
    private final Logger logger;

    private final ProxyDelegate<Player> proxyDelegate;
    private final ProxyMessageDelegate<Player> messageDelegate = (player, message) -> player.sendMessage(TextComponent.of(message));

    @Inject
    public QueuePlugin(ProxyServer proxy, Logger logger) {
        this.proxy = proxy;
        this.logger = logger;
        this.proxyDelegate = (uuid) -> proxy.getPlayer(uuid).orElse(null);
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        this.bootstrap = Bootstrap.create(this);
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        this.bootstrap.terminate();
    }

    public ProxyServer getProxy() {
        return this.proxy;
    }

    @Override
    public @NotNull Bootstrap bootstrap() {
        return this.bootstrap;
    }

    @Override
    public @NotNull QueueManager<Player, RegisteredServer> createQueueManager() {
        return new VelocityQueueManager(this);
    }

    @Override
    public @NotNull PriorityManager<Player> createPriorityManager() {
        return new PriorityManager<>(Player::hasPermission);
    }

    @Override
    public @NotNull ProxyDelegate<Player> getProxyDelegate() {
        return this.proxyDelegate;
    }

    @Override
    public @NotNull ProxyMessageDelegate<Player> getMessageDelegate() {
        return this.messageDelegate;
    }

    @Override
    public void registerListeners() {
        proxy.getEventManager().register(this, new VelocityConnectionListener());
    }
}
