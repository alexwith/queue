package me.hyfe.queue.commands;

import me.hyfe.queue.QueuePlugin;
import me.hyfe.queue.config.keys.LangKeys;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class JoinQueueCommand extends Command {
    private final QueuePlugin plugin;

    public JoinQueueCommand(QueuePlugin plugin) {
        super("joinqueue", null, "queuejoin", "queue");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;
        if (args.length != 1) {
            LangKeys.JOIN_QUEUE_USAGE.send(player, this.plugin.getMessageDelegate());
            return;
        }
        String server = args[0];
        ServerInfo serverInfo = this.plugin.getProxy().getServerInfo(server);
        if (serverInfo == null) {
            LangKeys.QUEUE_NOT_FOUND.send(player, this.plugin.getMessageDelegate());
            return;
        }
        player.connect(serverInfo);
    }
}
