package me.hyfe.queue;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import me.hyfe.queue.proxy.ConnectionListener;

import java.util.UUID;

public class VelocityConnectionListener extends ConnectionListener<VelocityQueueManager, Player, RegisteredServer, RegisteredServer> {

    @Subscribe
    public void onServerPreConnect(ServerPreConnectEvent event) {
        Player player = event.getPlayer();
        RegisteredServer target = event.getResult().getServer().get();
        RegisteredServer original = event.getOriginalServer();
        UUID uuid = player.getUniqueId();
        this.callConnect(player, uuid, original, () -> original.getServerInfo().getName(), target, target.getServerInfo().getName(), () -> {
            event.setResult(ServerPreConnectEvent.ServerResult.denied());
        }, player::hasPermission);
    }
}
