package me.hyfe.queue;

import me.hyfe.queue.proxy.ConnectionListener;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

public class BungeeConnectionListener extends ConnectionListener<BungeeQueueManager, ProxiedPlayer, Server, ServerInfo> implements Listener {

    @EventHandler
    public void onServerConnect(ServerConnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        ServerInfo target = event.getTarget();
        Server original = player.getServer();
        UUID uuid = player.getUniqueId();
        this.callConnect(player, uuid, original, () -> original.getInfo().getName(), target, target.getName(), () -> event.setCancelled(true), player::hasPermission);
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        this.callDisconnect(player, player.getUniqueId());
    }
}