package me.hyfe.queue.queue;

import me.hyfe.queue.proxy.ProxyDelegate;
import me.hyfe.queue.proxy.QueueProxyPlayer;
import me.hyfe.queue.redis.RedisProvider;

public class Queue<T> extends RedisQueue<QueueProxyPlayer<?>> {
    private final ProxyDelegate<T> proxyDelegate;

    public Queue(RedisProvider redisProvider, String key, ProxyDelegate<T> proxyDelegate) {
        super(redisProvider, key);
        this.proxyDelegate = proxyDelegate;
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
