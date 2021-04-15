package me.hyfe.queuenode.commands;

import me.hyfe.helper.Commands;
import me.hyfe.queuenode.Node;
import me.hyfe.queuenode.configs.ConfigKeys;
import me.hyfe.queuenode.configs.LangKeys;
import me.hyfe.queuenode.queue.QueueManager;

public class QueueCommand {
    private final QueueManager queueManager;

    public QueueCommand(Node node) {
        this.queueManager = node.getQueueManager();
        if (ConfigKeys.IS_HUB.get()) {
            this.createCommands();
        }
    }

    private void createCommands() {
        Commands.create("queue", "queuejoin", "joinqueue")
                .player()
                .description("Queue to join a server.")
                .subs(Commands.createSub()
                        .player()
                        .description("Join a queue.")
                        .argument(String.class, "queue")
                        .handler((player, context) -> {
                            String server = context.arg(0);
                            this.queueManager.queue(player, server);
                        })
                )
                .handler((player, context) -> {
                    context.sendUsage(player);
                });
        Commands.create("dequeue", "queueleave", "leavequeue")
                .player()
                .description("Leave your current queue.")
                .handler((player, context) -> {
                    this.queueManager.dequeue(player);
                    LangKeys.LEFT_QUEUE.send(player);
                });
    }
}
