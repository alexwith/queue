package me.hyfe.queue.queue;

import me.hyfe.queue.proxy.QueueProxyPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class QueueManager<T> {
    private final Map<String, Queue<T>> map = new HashMap<>();

    public abstract Queue<T> createQueue(String key);

    public Queue<T> getQueue(String server) {
        String key = server.concat("-queue");
        return this.map.get(key);
    }

    public void queue(T player, UUID uuid, String server) {
        String key = server.concat("-queue");
        if (!this.map.containsKey(key)) {
            this.map.put(key, this.createQueue(key));
        }
        QueueProxyPlayer<T> proxyPlayer = QueueProxyPlayer.of(player, uuid);
        Queue<T> queue = this.map.get(server);
        queue.queue(proxyPlayer);
    }
}
