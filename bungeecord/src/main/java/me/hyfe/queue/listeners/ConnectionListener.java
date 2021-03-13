package me.hyfe.queue.listeners;

import me.hyfe.queue.BungeeQueueManager;
import me.hyfe.queue.bootstrap.Bootstrap;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ConnectionListener implements Listener {
    private final BungeeQueueManager queueManager = Bootstrap.get().getQueueManager();

    @EventHandler
    public void onServerConnect(ServerConnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        ServerInfo target = event.getTarget();
        UUID uuid = player.getUniqueId();
        CompletableFuture.runAsync(() -> {
            boolean isInQueue = this.queueManager.isInQueue(uuid).join();
            if (isInQueue) {
                return;
            }
            this.queueManager.queue(player, uuid, target.getSocketAddress().toString(), target).join();
        }).exceptionally((ex) -> {
            ex.printStackTrace();
            return null;
        });
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();

    }
}