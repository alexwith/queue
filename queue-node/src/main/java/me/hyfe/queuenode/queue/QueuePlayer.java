package me.hyfe.queuenode.queue;

import me.hyfe.helper.uuid.FastUUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class QueuePlayer implements Comparable<QueuePlayer> {
    private final UUID uuid;
    private final int priority;

    public QueuePlayer(UUID uuid, int priority) {
        this.uuid = uuid;
        this.priority = priority;
    }

    public static QueuePlayer of(UUID uuid, int priority) {
        return new QueuePlayer(uuid, priority);
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String encode() {
        return FastUUID.toString(this.uuid) + ":" + this.priority;
    }

    public static QueuePlayer decode(String encoded) {
        String[] args = encoded.split(":");
        UUID uuid = FastUUID.parse(args[0]);
        return new QueuePlayer(uuid, Integer.parseInt(args[1]));
    }

    @Override
    public int compareTo(@NotNull QueuePlayer player) {
        return this.priority == player.priority ? 1 : Integer.compare(this.priority, player.priority);
    }
}
