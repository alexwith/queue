package me.hyfe.queue.queue;

import me.hyfe.queue.proxy.ProxyDelegate;
import me.hyfe.queue.proxy.QueueProxyPlayer;
import me.hyfe.queue.proxy.ServerSender;
import me.hyfe.queue.redis.RedisProvider;

public class Queue<T, U> extends RedisQueue<QueueProxyPlayer<?>> {
    private final ProxyDelegate<T> proxyDelegate;
    private final ServerSender<T, U> serverSender;

    public Queue(RedisProvider redisProvider, String key, ProxyDelegate<T> proxyDelegate, ServerSender<T, U> serverSender) {
        super(redisProvider, key);
        this.proxyDelegate = proxyDelegate;
        this.serverSender = serverSender;
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
}
