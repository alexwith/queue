package me.hyfe.queue.queue;

import me.hyfe.queue.proxy.QueueProxyPlayer;
import me.hyfe.queue.proxy.ServerSender;
import me.hyfe.queue.proxy.delegates.ProxyDelegate;
import me.hyfe.queue.redis.RedisProvider;

public class Queue<T, U> extends RedisQueue<QueueProxyPlayer<?>> {
    private final String server;
    private final ProxyDelegate<T> proxyDelegate;
    private final ServerSender<T, U> serverSender;

    private boolean isPaused = false;

    public Queue(RedisProvider redisProvider, String key, String server, ProxyDelegate<T> proxyDelegate, ServerSender<T, U> serverSender) {
        super(redisProvider, key);
        this.server = server;
        this.proxyDelegate = proxyDelegate;
        this.serverSender = serverSender;
    }

    public String getServer() {
        return this.server;
    }

    public ServerSender<T, U> getServerSender() {
        return this.serverSender;
    }

    @Override
    public String encode(QueueProxyPlayer<?> value) {
        return value.encode();
    }

    @Override
    public QueueProxyPlayer<?> decode(String string) {
        return QueueProxyPlayer.decode(this.proxyDelegate, string);
    }

    public boolean isPaused() {
        return this.isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }
}
