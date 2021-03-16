package me.hyfe.queue.proxy;

import me.hyfe.queue.bootstrap.Bootstrap;
import me.hyfe.queue.config.keys.ConfigKeys;
import me.hyfe.queue.config.keys.LangKeys;
import me.hyfe.queue.proxy.delegates.ProxyMessageDelegate;
import me.hyfe.queue.queue.Queue;
import me.hyfe.queue.queue.QueueManager;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ConnectionListener<QM extends QueueManager<P, T>, P, S, T> {
    private final QM queueManager;
    private final ProxyMessageDelegate<P> messageDelegate;

    public ConnectionListener() {
        this.queueManager = Bootstrap.get().getQueueManager();
        this.messageDelegate = this.queueManager.getMessageDelegate();
    }

    public void callConnect(P player, UUID uuid, S original, Supplier<String> originalName, T target, String targetName, Runnable cancel, Predicate<String> canBypass) {
        if (original == null || ConfigKeys.HUBS.get().contains(targetName) || this.queueManager.isIntTransit(player)) {
            return;
        }
        if (canBypass.test(ConfigKeys.BYPASS_PERMISSION.get())) {
            LangKeys.BYPASSING_QUEUE.send(player, this.messageDelegate);
            return;
        }/* else if (isRestricted) {
            LangKeys.SERVER_WHITELISTED.send(player, this.messageDelegate, replacer -> replacer
                    .set("server", name)
            );
            return;
        }*/
        cancel.run();
        if (originalName.get().equals(targetName)) {
            LangKeys.ALREADY_ON_SERVER.send(player, this.messageDelegate, replacer -> replacer
                    .set("server", targetName)
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
            this.queueManager.queue(player, uuid, targetName, target).join();
        }).exceptionally((ex) -> {
            ex.printStackTrace();
            return null;
        });
    }

    public void callDisconnect(P player, UUID uuid) {
        this.queueManager.dequeue(player, uuid);
    }
}
