package me.hyfe.queuenode.queue;

import me.hyfe.queuenode.Node;

public class Queue extends RedisQueue<QueuePlayer> {
    private final String server;
    private final String pauseKey;

    public Queue(Node node, String key, String server) {
        super(node, key);
        this.server = server;
        this.pauseKey = this.key.concat("pause");
    }

    public String getServer() {
        return this.server;
    }

    @Override
    public String encode(QueuePlayer value) {
        return value.encode();
    }

    @Override
    public QueuePlayer decode(String string) {
        return QueuePlayer.decode(string);
    }

    public boolean isPaused() {
        return this.redis.provideJedis((jedis) -> {
            return Boolean.parseBoolean(jedis.get(this.pauseKey))
        });
    }

    public void setPaused(boolean paused) {
        this.redis.provideJedis((jedis) -> {
            jedis.set(this.pauseKey, Boolean.toString(paused));
        });
    }
}
