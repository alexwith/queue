package me.hyfe.queue.queue.tasks;

import me.hyfe.queue.bootstrap.Bootstrap;
import me.hyfe.queue.config.keys.ConfigKeys;
import me.hyfe.queue.config.keys.LangKeys;
import me.hyfe.queue.proxy.QueueProxyPlayer;
import me.hyfe.queue.proxy.ServerSender;
import me.hyfe.queue.queue.Queue;
import me.hyfe.queue.queue.QueueManager;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class QueueTask<T, U> implements Runnable {
    private final QueueManager<T, U> queueManager;
    private final Queue<T, U> queue;
    private final ServerSender<T, ?> serverSender;
    private final ScheduledFuture<?> task;

    public QueueTask(QueueManager<T, U> queueManager, Queue<T, U> queue) {
        this.queueManager = queueManager;
        this.queue = queue;
        this.serverSender = queue.getServerSender();
        this.task = Bootstrap.SCHEDULED_EXECUTOR.scheduleAtFixedRate(this, ConfigKeys.POLL_INTERVAL.get(), ConfigKeys.POLL_INTERVAL.get(), TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        if (this.queue.length() == 0 || this.queue.isPaused()) {
            return;
        }
        if (!this.queueManager.isOnline(this.queue.getServer())) {
            for (int i = 0; i < this.queue.length(); i++) {
                QueueProxyPlayer<T> queuedPlayer = (QueueProxyPlayer<T>) this.queue.poll();
                if (queuedPlayer == null) {
                    break;
                }
                LangKeys.SERVER_OFFLINE.send(queuedPlayer.getPlayer(), this.queueManager.getMessageDelegate());
            }
            return;
        }
        QueueProxyPlayer<T> proxyPlayer = (QueueProxyPlayer<T>) this.queue.poll();
        if (proxyPlayer == null) {
            return;
        }
        T player = proxyPlayer.getPlayer();
        this.queueManager.callTransit(player, false);
        LangKeys.SENDING_SERVER.send(player, this.queueManager.getMessageDelegate(), replacer -> replacer
                .set("server", this.queue.getServer())
        );
        this.serverSender.accept(player);
        this.queueManager.callTransit(player, true);
    }

    public void terminate() {
        this.task.cancel(true);
    }
}
