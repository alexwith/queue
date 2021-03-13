package me.hyfe.queue.proxy;

import me.hyfe.helper.uuid.FastUUID;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class QueueProxyPlayer<T> implements Comparable<QueueProxyPlayer<?>> {
    private final T player;
    private final UUID uuid;
    private final int priority;

    public QueueProxyPlayer(T player, UUID uuid, int priority) {
        this.player = player;
        this.uuid = uuid;
        this.priority = priority;
    }

    public static <U> QueueProxyPlayer<U> of(U player, UUID uuid) {
        return new QueueProxyPlayer<>(player, uuid, 0);
    }

    public T getPlayer() {
        return this.player;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String encode() {
        return FastUUID.toString(this.uuid) + ":" + this.priority;
    }

    public static <U> QueueProxyPlayer<U> decode(ProxyDelegate<U> proxyDelegate, String encoded) {
        String[] args = encoded.split(":");
        UUID uuid = FastUUID.parse(args[0]);
        return new QueueProxyPlayer<>(proxyDelegate.getPlayer(uuid), uuid, Integer.parseInt(args[1]));
    }

    @Override
    public int compareTo(@NotNull QueueProxyPlayer<?> player) {
        return this.priority < player.priority ? 1 : -1;
    }
}
