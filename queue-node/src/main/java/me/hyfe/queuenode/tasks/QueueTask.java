package me.hyfe.queuenode.tasks;


import me.hyfe.helper.Schedulers;
import me.hyfe.helper.scheduler.Task;
import me.hyfe.helper.terminable.Terminable;
import me.hyfe.queuenode.configs.ConfigKeys;
import me.hyfe.queuenode.configs.LangKeys;
import me.hyfe.queuenode.queue.Queue;
import me.hyfe.queuenode.queue.QueueManager;
import me.hyfe.queuenode.queue.QueuePlayer;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class QueueTask implements Runnable, Terminable {
    private final QueueManager queueManager;
    private final Queue queue;
    private final Task task;

    public QueueTask(QueueManager queueManager, Queue queue) {
        this.queueManager = queueManager;
        this.queue = queue;
        this.task = Schedulers.async().runRepeating(this, ConfigKeys.POLL_INTERVAL.get(), TimeUnit.MILLISECONDS, ConfigKeys.POLL_INTERVAL.get(), TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        if (this.queue.length() == 0 || this.queue.isPaused()) {
            return;
        }
        if (!this.queueManager.isOnline(this.queue.getServer())) {
            for (int i = 0; i < this.queue.length(); i++) {
                QueuePlayer queuedPlayer = this.queue.poll();
                if (queuedPlayer == null) {
                    break;
                }
                LangKeys.SERVER_OFFLINE.send(queuedPlayer.getPlayer());
            }
            return;
        }
        QueuePlayer queuePlayer = this.queue.poll();
        if (queuePlayer == null) {
            return;
        }
        Player player = queuePlayer.getPlayer();
        this.queueManager.callTransit(player.getUniqueId(), false);
        LangKeys.SENDING_SERVER.send(player, replacer -> replacer
                .set("server", this.queue.getServer())
        );
        //this.serverSender.accept(player);
        this.queueManager.callTransit(player.getUniqueId(), true);
    }

    @Override
    public void close() {
        this.task.close();
    }
}
