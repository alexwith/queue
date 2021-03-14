package me.hyfe.queue.proxy;

import me.hyfe.queue.config.keys.ConfigKeys;
import me.hyfe.queue.config.keys.LangKeys;
import me.hyfe.queue.proxy.delegates.ProxyMessageDelegate;
import me.hyfe.queue.queue.Queue;
import me.hyfe.queue.queue.QueueManager;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class ConnectionListener<QM extends QueueManager<P, T>, P, S, T> {
    private final QM queueManager;
    private final ProxyMessageDelegate<P> messageDelegate;

    public ConnectionListener(QM queueManager) {
        this.queueManager = queueManager;
        this.messageDelegate = queueManager.getMessageDelegate();
    }

    public void callConnect(P player, UUID uuid, S playerServer, Supplier<String> playerServerName, T target, String name, Runnable cancel, Predicate<String> canBypass, boolean isRestricted) {
        if (playerServer == null || ConfigKeys.HUBS.get().contains(name) || this.queueManager.isIntTransit(player)) {
            return;
        }
        if (canBypass.test(ConfigKeys.BYPASS_PERMISSION.get())) {
            LangKeys.BYPASSING_QUEUE.send(player, this.messageDelegate);
            return;
        } else if (isRestricted) {
            LangKeys.SERVER_WHITELISTED.send(player, this.messageDelegate, replacer -> replacer
                    .set("server", name)
            );
            return;
        }
        cancel.run();
        if (playerServerName.get().equals(name)) {
            LangKeys.ALREADY_ON_SERVER.send(player, this.messageDelegate, replacer -> replacer
                    .set("server", name)
            );
            return;
        }
        CompletableFuture.runAsync(() -> {
            Queue<P, T> queue = this.queueManager.getInQueue(uuid).join();
            if (queue != null) {
                LangKeys.ALREADY_QUEUED.send(player, this.messageDelegate, replacer -> replacer
                        .set("server", queue.getServer())
                );
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
