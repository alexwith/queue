package me.hyfe.queue.listeners;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ConnectionListener implements Listener {

    @EventHandler
    public void onServerConnect(ServerConnectedEvent event) {
        ProxiedPlayer player = event.getPlayer();
        Server server = event.getServer();
        ProxyServer.getInstance().broadcast("socket thing: ");
        System.out.println("socket:" + server.getInfo().getSocketAddress().toString());
        //Bootstrap.get().getQueueManager().queue(player, server.getSocketAddress().toString());
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        
    }
}
