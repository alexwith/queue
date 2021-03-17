package me.hyfe.queue.commands;

import me.hyfe.helper.Commands;
import me.hyfe.helper.command.command.SubCommand;
import me.hyfe.queue.HubPlugin;
import org.bukkit.entity.Player;

public class QueueCommand {
    private final HubPlugin plugin;

    public QueueCommand(HubPlugin plugin) {
        this.plugin = plugin;
        this.createCommand();
    }

    private void createCommand() {
        Commands.create("queue")
                .subs(
                        this.serverSub()
                )
                .handler((sender, context) -> {

                }).register();
    }

    private SubCommand<Player> serverSub() {
        return Commands.createSub()
                .player()
                .argument(String.class, "server")
                .handler((sender, context) -> {
                    String server = context.arg(0);
                    this.plugin.getServerSender().accept(sender, server);
                });
    }
}
