package me.hyfe.queuenode.commands;

import me.hyfe.helper.Commands;
import me.hyfe.queuenode.Node;
import me.hyfe.queuenode.configs.ConfigKeys;
import me.hyfe.queuenode.configs.LangKeys;
import me.hyfe.queuenode.pause.PauseReason;
import me.hyfe.queuenode.queue.Queue;
import me.hyfe.queuenode.queue.QueueManager;

public class QueueAdminCommand {
    private final QueueManager queueManager;

    public QueueAdminCommand(Node node) {
        this.queueManager = node.getQueueManager();
        if (ConfigKeys.IS_HUB.get()) {
            this.createCommand();
        }
    }

    private void createCommand() {
        Commands.create("queueadmin")
                .description("Manage queues.")
                .permission("queue.admin")
                .subs(Commands.createSub()
                                .description("Pause a queue.")
                                .argument("pause")
                                .argument(String.class, "queue")
                                .handler((sender, context) -> {
                                    String server = context.arg(1);
                                    Queue queue = this.queueManager.getQueue(server);
                                    if (queue == null) {
                                        LangKeys.QUEUE_NOT_FOUND.send(sender);
                                        return;
                                    }
                                    queue.setPaused(true, PauseReason.MANUAL);
                                    LangKeys.QUEUE_PAUSED.send(sender);
                                }), Commands.createSub()
                                .description("Resume a queue.")
                                .argument("resume")
                                .argument(String.class, "queue")
                                .handler((sender, context) -> {
                                    String server = context.arg(1);
                                    Queue queue = this.queueManager.getQueue(server);
                                    if (queue == null) {
                                        LangKeys.QUEUE_NOT_FOUND.send(sender);
                                        return;
                                    }
                                    queue.setPaused(false, PauseReason.MANUAL);
                                    LangKeys.QUEUE_RESUMED.send(sender);
                                }),
                        Commands.createSub()
                                .description("Clear a queue.")
                                .argument("clear")
                                .argument(String.class, "queue")
                                .handler((sender, context) -> {
                                    String server = context.arg(1);
                                    Queue queue = this.queueManager.getQueue(server);
                                    if (queue == null) {
                                        LangKeys.QUEUE_NOT_FOUND.send(sender);
                                        return;
                                    }
                                    queue.clear();
                                    LangKeys.QUEUE_CLEARED.send(sender);
                                })
                )
                .handler((player, context) -> {
                    context.sendUsage(player);
                });
    }
}
