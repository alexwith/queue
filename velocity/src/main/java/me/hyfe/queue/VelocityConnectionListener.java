package me.hyfe.queue;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import me.hyfe.queue.proxy.ConnectionListener;

import java.util.UUID;

public class VelocityConnectionListener extends ConnectionListener<VelocityQueueManager, Player, RegisteredServer, RegisteredServer> {

    @Subscribe
    public void onServerPreConnect(ServerPreConnectEvent event) {
        Player player = event.getPlayer();
        RegisteredServer target = event.getResult().getServer().get();
        ServerConnection originalConnection = player.getCurrentServer().orElse(null);
        if (originalConnection == null) {
            return;
        }
        RegisteredServer original = originalConnection.getServer();
        UUID uuid = player.getUniqueId();

        System.out.println(target);
        System.out.println(original);
        System.out.println(uuid);

        this.callConnect(player, uuid, original, () -> original.getServerInfo().getName(), target, target.getServerInfo().getName(), () -> {
            event.setResult(ServerPreConnectEvent.ServerResult.denied());
        }, player::hasPermission);
    }

    @Subscribe
    public void onPlayerDisconnect(DisconnectEvent event) {
        Player player = event.getPlayer();
        this.callDisconnect(player, player.getUniqueId());
    }
}
