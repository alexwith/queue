package me.hyfe.queuenode.queue;

import me.hyfe.queuenode.Node;

public class Queue extends RedisQueue<QueuePlayer> {
    private final String server;

    private boolean isPaused = false;

    public Queue(Node node, String key, String server) {
        super(node, key);
        this.server = server;
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
        return this.isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }
}
