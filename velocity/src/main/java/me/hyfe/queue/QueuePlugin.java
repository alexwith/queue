package me.hyfe.queue;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import me.hyfe.queue.bootstrap.Bootstrap;
import me.hyfe.queue.bootstrap.BootstrapProvider;
import me.hyfe.queue.listeners.ConnectionListener;
import me.hyfe.queue.proxy.delegates.ProxyDelegate;
import me.hyfe.queue.proxy.delegates.ProxyMessageDelegate;
import me.hyfe.queue.queue.QueueManager;
import net.kyori.text.TextComponent;
import org.jetbrains.annotations.NotNull;

@Plugin(id = "queue", name = "Queue", version = "1.0.0", authors = {"hyfe"})
public class QueuePlugin implements BootstrapProvider<Player, ServerInfo> {
    private final Bootstrap bootstrap;

    private final ProxyDelegate<Player> proxyDelegate;
    private final ProxyMessageDelegate<Player> messageDelegate = new ProxyMessageDelegate<Player>() {

        @Override
        public void messageDelegate(Player player, String message) {
            player.sendMessage(TextComponent.of(message));
        }
    };

    @Inject
    public QueuePlugin(ProxyServer server) {
        this.bootstrap = Bootstrap.create(this);
        this.proxyDelegate = (uuid) -> server.getPlayer(uuid).orElse(null);
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        this.bootstrap.terminate();
    }

    @Override
    public @NotNull Bootstrap bootstrap() {
        return this.bootstrap;
    }

    @Override
    public @NotNull QueueManager<Player, ServerInfo> createQueueManager() {
        return null;
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
        new ConnectionListener();
    }
}
