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
import java.util.function.Consumer;

public class QueueTask implements Runnable, Terminable {
    private final QueueManager queueManager;
    private final Queue queue;
    private final Task task;

    public QueueTask(QueueManager queueManager, Queue queue) {
        this.queueManager = queueManager;
        this.queue = queue;
        System.out.println("create queue task: " + ConfigKeys.POLL_INTERVAL.get());
        this.task = Schedulers.async().runRepeating(this, ConfigKeys.POLL_INTERVAL.get(), TimeUnit.MILLISECONDS, ConfigKeys.POLL_INTERVAL.get(), TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        if (this.queue.length() == 0) {
            return;
        }
        String server = this.queue.getServer();
        if (this.queue.isPaused()) {
            if (this.queueManager.isOnline(server)) {
                this.queue.setPaused(false);
            } else {
                return;
            }
        }
        if (!this.queueManager.isOnline(server)) {
            this.consumeAndPauseQueue((player) -> {
                LangKeys.SERVER_OFFLINE.send(player.getPlayer());
            });
            return;
        }
        if (this.queueManager.isWhitelisted(server)) {
            this.consumeAndPauseQueue((player) -> {
                LangKeys.SERVER_WHITELISTED.send(player.getPlayer());
            });
            return;
        }
        QueuePlayer queuePlayer = this.queue.poll();
        if (queuePlayer == null) {
            return;
        }
        Player player = queuePlayer.getPlayer();
        LangKeys.SENDING_SERVER.send(player, replacer -> replacer
                .set("server", this.queue.getServer())
        );
        this.queueManager.sendToServer(player, server);
    }

    @Override
    public void close() {
        this.task.close();
    }

    private void consumeAndPauseQueue(Consumer<QueuePlayer> consumer) {
        this.queue.setPaused(true);
        for (int i = 0; i < this.queue.length(); i++) {
            QueuePlayer player = this.queue.get(i);
            if (player == null) {
                continue;
            }
            consumer.accept(player);
        }
    }
}
