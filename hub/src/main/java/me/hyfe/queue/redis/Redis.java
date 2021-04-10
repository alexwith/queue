package me.hyfe.queue.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.function.Consumer;
import java.util.function.Function;

public class Redis {
    private final JedisPool pool;

    public Redis(Credentials credentials) throws JedisConnectionException {
        this.pool = this.createPool(credentials);
        try (Jedis jedis = this.pool.getResource()) {
            jedis.ping();
        }
    }

    public static Redis createInstance(Credentials credentials) throws JedisConnectionException {
        return new Redis(credentials);
    }

    public JedisPool getPool() {
        return this.pool;
    }

    public void provideJedis(Consumer<Jedis> consumer) {
        try (Jedis jedis = this.pool.getResource()) {
            consumer.accept(jedis);
        }
    }

    public <T> T provideJedis(Function<Jedis, T> function) {
        try (Jedis jedis = this.pool.getResource()) {
            return function.apply(jedis);
        }
    }

    public void close() {
        if (this.pool != null) {
            this.pool.close();
        }
    }

    private JedisPool createPool(Credentials credentials) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(16);
        if (credentials.getPassword().isEmpty()) {
            return new JedisPool(config, credentials.getAddress(), credentials.getPort(), 2000);
        } else {
            return new JedisPool(config, credentials.getAddress(), credentials.getPort(), 2000, credentials.getPassword());
        }
    }
}
