package me.hyfe.queue.proxy;

import me.hyfe.queue.config.keys.ConfigKeys;
import me.hyfe.queue.config.keys.LangKeys;
import me.hyfe.queue.queue.QueueManager;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public abstract class ConnectionListener<QM extends QueueManager<P, T>, P, S, T> {
    private final QM queueManager;

    public ConnectionListener(QM queueManager) {
        this.queueManager = queueManager;
    }

    public void callConnect(P player, UUID uuid, S playerServer, Supplier<String> playerServerName, T target, String name, Runnable cancel) {
        if (playerServer == null || ConfigKeys.HUBS.get().contains(name) || this.queueManager.isIntTransit(player)) {
            return;
        }
        cancel.run();
        if (playerServerName.get().equals(name)) {
            LangKeys.ALREADY_ON_SERVER.send(player, this.queueManager.getMessageDelegate(), replacer -> replacer
                    .set("server", name)
            );
            return;
        }
        CompletableFuture.runAsync(() -> {
            boolean isInQueue = this.queueManager.isInQueue(uuid).join();
            if (isInQueue) {
                return;
            }
            this.queueManager.queue(player, uuid, name, target).join();
        }).exceptionally((ex) -> {
            ex.printStackTrace();
            return null;
        });
    }

    public void callDisconnect(P player, UUID uuid) {
        this.queueManager.dequeue(player, uuid);
    }
}
