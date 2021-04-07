package me.hyfe.queue.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import me.hyfe.queue.QueuePlugin;
import me.hyfe.queue.config.keys.LangKeys;

public class JoinQueueCommand implements SimpleCommand {
    private final QueuePlugin plugin;

    public JoinQueueCommand(QueuePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (!(source instanceof Player)) {
            return;
        }
        Player player = (Player) source;
        if (args.length != 1) {
            LangKeys.JOIN_QUEUE_USAGE.send(player, this.plugin.getMessageDelegate());
            return;
        }
        String server = args[0];
        RegisteredServer registeredServer = this.plugin.getProxy().getServer(server).orElse(null);
        if (registeredServer == null) {
            LangKeys.QUEUE_NOT_FOUND.send(player, this.plugin.getMessageDelegate());
            return;
        }
        player.createConnectionRequest(registeredServer);
    }
}
