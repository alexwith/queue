package me.hyfe.queue;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import me.hyfe.queue.proxy.ServerSender;

public class VelocityServerSender extends ServerSender<Player, RegisteredServer> {

    public VelocityServerSender(RegisteredServer target) {
        super(target);
    }

    @Override
    public void accept(Player player) {
        player.createConnectionRequest(this.target).connectWithIndication().join();
    }
}
