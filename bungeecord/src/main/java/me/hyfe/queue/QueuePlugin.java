package me.hyfe.queue;

import me.hyfe.queue.bootstrap.Bootstrap;
import me.hyfe.queue.bootstrap.BootstrapProvider;
import me.hyfe.queue.listeners.ConnectionListener;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class QueuePlugin extends Plugin implements BootstrapProvider {
    private Bootstrap bootstrap;

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
    public void registerListeners() {
        this.getProxy().getPluginManager().registerListener(this, new ConnectionListener());
    }
}
