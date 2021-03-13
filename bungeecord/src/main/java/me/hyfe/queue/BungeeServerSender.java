package me.hyfe.queue;

import me.hyfe.queue.proxy.ServerSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BungeeServerSender extends ServerSender<ProxiedPlayer, ServerInfo> {

    public BungeeServerSender(ServerInfo target) {
        super(target);
    }

    @Override
    public void accept(ProxiedPlayer player) {
        player.connect(this.target);
    }
}
