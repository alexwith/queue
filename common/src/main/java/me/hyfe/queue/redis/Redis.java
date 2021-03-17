package me.hyfe.queue.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class Redis {
    private final JedisPool pool;

    public Redis(Credentials credentials) {
        this.pool = this.createPool(credentials);
        try (Jedis jedis = this.pool.getResource()) {
            jedis.ping();
        } catch (JedisConnectionException ex) {
            ex.printStackTrace();
        }
    }

    public static Redis createInstance(Credentials credentials) {
        return new Redis(credentials);
    }

    public JedisPool getPool() {
        return this.pool;
    }

    public Jedis getJedis() {
        return this.pool.getResource();
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
