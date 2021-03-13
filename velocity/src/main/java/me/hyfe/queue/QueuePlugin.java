package me.hyfe.queue;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import me.hyfe.queue.bootstrap.Bootstrap;
import me.hyfe.queue.bootstrap.BootstrapProvider;
import me.hyfe.queue.listeners.ConnectionListener;
import org.jetbrains.annotations.NotNull;

@Plugin(id = "queue", name = "Queue", version = "1.0.0", authors = {"hyfe"})
public class QueuePlugin implements BootstrapProvider {
    private final Bootstrap bootstrap;

    public QueuePlugin() {
        this.bootstrap = Bootstrap.create(this);
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
    public void registerListeners() {
        new ConnectionListener();
    }
}
