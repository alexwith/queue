package me.hyfe.queue.queue;

import me.hyfe.queue.config.holders.ConfigKeys;
import me.hyfe.queue.proxy.QueueProxyPlayer;
import me.hyfe.queue.proxy.ServerSender;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class QueueTask<T, U> implements Runnable {
    private final Queue<T, U> queue;
    private final ServerSender<T, ?> serverSender;
    private final ScheduledFuture<?> task;

    private static final ScheduledExecutorService SCHEDULER = Executors.newSingleThreadScheduledExecutor();

    public QueueTask(Queue<T, U> queue) {
        this.queue = queue;
        this.serverSender = queue.getServerSender();
        this.task = SCHEDULER.scheduleAtFixedRate(this, ConfigKeys.POLL_INTERVAL.get(), ConfigKeys.POLL_INTERVAL.get(), TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        if (this.queue.length() == 0) {
            return;
        }
        QueueProxyPlayer<T> proxyPlayer = (QueueProxyPlayer<T>) this.queue.poll();
        if (proxyPlayer == null) {
            return;
        }
        T player = proxyPlayer.getPlayer();
        this.serverSender.accept(player);
    }

    public void terminate() {
        this.task.cancel(true);
    }
}
