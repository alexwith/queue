package me.hyfe.queue.queue.tasks;

import me.hyfe.queue.config.keys.ConfigKeys;
import me.hyfe.queue.proxy.QueueProxyPlayer;
import me.hyfe.queue.queue.Queue;
import me.hyfe.queue.queue.QueueManager;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class PositionTask<T, U> implements Runnable {
    private final QueueManager<T, U> queueManager;
    private final ScheduledFuture<?> task;

    private static final ScheduledExecutorService SCHEDULER = Executors.newSingleThreadScheduledExecutor();

    public PositionTask(QueueManager<T, U> queueManager) {
        this.queueManager = queueManager;
        this.task = SCHEDULER.scheduleAtFixedRate(this, ConfigKeys.POSITION_INTERVAL.get(), ConfigKeys.POSITION_INTERVAL.get(), TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        for (Queue<T, U> queue : this.queueManager.getQueues()) {
            for (int i = 0; i < queue.length(); i++) {
                QueueProxyPlayer<T> proxyPlayer = (QueueProxyPlayer<T>) queue.get(i);
                this.queueManager.sendPosition(proxyPlayer.getPlayer(), proxyPlayer.getUuid());
            }
        }
    }

    public void terminate() {
        this.task.cancel(true);
    }
}
