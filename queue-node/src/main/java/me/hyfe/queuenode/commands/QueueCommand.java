package me.hyfe.queuenode.commands;

import me.hyfe.helper.Commands;
import me.hyfe.queuenode.Node;
import me.hyfe.queuenode.queue.Queue;
import me.hyfe.queuenode.queue.QueueManager;

public class QueueCommand {
    private final QueueManager queueManager;

    public QueueCommand(Node node) {
        this.queueManager = node.getQueueManager();
        this.createCommand();
    }

    private void createCommand() {
        Commands.create("queue", "queuejoin", "joinqueue")
                .player()
                .subs(Commands.createSub()
                        .player()
                        .argument(String.class, "queue")
                        .handler((player, context) -> {
                            String server = context.arg(1);
                            Queue queue = this.queueManager.getQueue(server);
                            
                        })
                )
                .handler((player, context) -> {
                    context.sendUsage(player);
                });
    }
}
