package me.hyfe.queue;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import me.hyfe.queue.bootstrap.Bootstrap;
import me.hyfe.queue.bootstrap.BootstrapProvider;
import me.hyfe.queue.proxy.delegates.ProxyDelegate;
import me.hyfe.queue.proxy.delegates.ProxyMessageDelegate;
import me.hyfe.queue.queue.QueueManager;
import net.kyori.text.TextComponent;
import org.jetbrains.annotations.NotNull;

@Plugin(id = "queue", name = "Queue", version = "1.0.0", authors = {"hyfe"})
public class QueuePlugin implements BootstrapProvider<Player, RegisteredServer> {
    private final Bootstrap bootstrap;
    private final ProxyServer proxy;

    private final ProxyDelegate<Player> proxyDelegate;
    private final ProxyMessageDelegate<Player> messageDelegate = (player, message) -> player.sendMessage(TextComponent.of(message));

    @Inject
    public QueuePlugin(ProxyServer proxy) {
        this.proxy = proxy;
        this.bootstrap = Bootstrap.create(this);
        this.proxyDelegate = (uuid) -> proxy.getPlayer(uuid).orElse(null);
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
    public @NotNull ProxyDelegate<Player> getProxyDelegate() {
        return this.proxyDelegate;
    }

    @Override
    public @NotNull ProxyMessageDelegate<Player> getMessageDelegate() {
        return this.messageDelegate;
    }

    @Override
    public void registerListeners() {
        new VelocityConnectionListener();
    }
}
