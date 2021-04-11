package me.hyfe.queuenode.tasks;


import me.hyfe.helper.Schedulers;
import me.hyfe.helper.scheduler.Task;
import me.hyfe.helper.terminable.Terminable;
import me.hyfe.queuenode.Node;
import me.hyfe.queuenode.configs.ConfigKeys;
import me.hyfe.queuenode.queue.Queue;
import me.hyfe.queuenode.queue.QueueManager;
import me.hyfe.queuenode.queue.QueuePlayer;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class PositionTask implements Runnable, Terminable {
    private final QueueManager queueManager;
    private final Task task;

    public PositionTask(Node node) {
        this.queueManager = node.getQueueManager();
        this.task = Schedulers.async().runRepeating(this, ConfigKeys.POSITION_INTERVAL.get(), TimeUnit.MILLISECONDS, ConfigKeys.POSITION_INTERVAL.get(), TimeUnit.MILLISECONDS);
    }

    public static Optional<PositionTask> tryStart(Node node) {
        return ConfigKeys.IS_HUB.get() ? Optional.empty() : Optional.of(new PositionTask(node));
    }

    @Override
    public void run() {
        for (Queue queue : this.queueManager.getQueues()) {
            for (int i = 0; i < queue.length(); i++) {
                QueuePlayer proxyPlayer = queue.get(i);
                this.queueManager.sendPosition(proxyPlayer.getPlayer(), proxyPlayer.getUuid()).join();
            }
        }
    }

    @Override
    public void close() {
        this.task.close();
    }
}
