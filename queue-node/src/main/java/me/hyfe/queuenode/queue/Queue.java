package me.hyfe.queuenode.queue;

import me.hyfe.queuenode.Node;
import me.hyfe.queuenode.pause.Pause;
import me.hyfe.queuenode.pause.PauseReason;

public class Queue extends RedisQueue<QueuePlayer> {
    private final String server;
    private final String pauseKey;

    public Queue(Node node, String key, String server) {
        super(node, key);
        this.server = server;
        this.pauseKey = this.key.concat("-pause");
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
        return this.getPause().isPaused();
    }

    public PauseReason getPauseReason() {
        return this.getPause().getReason();
    }

    public Pause getPause() {
        return this.redis.provideJedis((jedis) -> {
            String value = jedis.get(this.pauseKey);
            if (value == null) {
                return new Pause(false, PauseReason.AUTO);
            }
            String[] values = value.split("%");
            boolean paused = Boolean.parseBoolean(values[0]);
            PauseReason reason = PauseReason.valueOf(values[1]);
            return new Pause(paused, reason);
        });
    }

    public void setPaused(boolean paused, PauseReason reason) {
        this.redis.provideJedis((jedis) -> {
            jedis.set(this.pauseKey, paused + "%" + reason.toString());
        });
    }
}
