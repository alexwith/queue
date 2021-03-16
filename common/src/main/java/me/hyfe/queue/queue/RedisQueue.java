package me.hyfe.queue.queue;

import me.hyfe.queue.redis.RedisProvider;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ListPosition;

public abstract class RedisQueue<V extends Comparable<V>> {
    private final Jedis jedis;
    private final String key;

    public RedisQueue(RedisProvider redisProvider, String key) {
        this.jedis = redisProvider.getRedis().getJedis();
        this.key = key;
    }

    public abstract String encode(V value);

    public abstract V decode(String string);

    public void queue(V value) {
        long search = this.binarySearch(value);
        long index = search < 0 ? -(search + 1) : search + 1;
        if (index < this.length()) {
            V pivot = this.get(index);
            this.jedis.linsert(this.key, ListPosition.BEFORE, this.encode(pivot), this.encode(value));
        } else {
            this.jedis.rpush(this.key, this.encode(value));
        }
    }

    public void dequeue(V value) {
        this.jedis.lrem(this.key, 0, this.encode(value));
    }

    public void clear() {
        this.jedis.del(this.key);
    }

    public V peek() {
        return this.get(0);
    }

    public V poll() {
        return this.decode(this.jedis.lpop(this.key));
    }

    public V get(long index) {
        return this.decode(this.jedis.lindex(this.key, index));
    }

    public void set(long index, V value) {
        if (index > this.length() - 1) {
            this.jedis.lpush(this.key, this.encode(value));
        } else {
            this.jedis.lset(this.key, index, this.encode(value));
        }
    }

    public void remove(V value) {
        this.jedis.lrem(this.key, 0, this.encode(value));
    }

    public int indexOf(V value) {
        int index = 0;
        String rawValue = this.encode(value);
        for (int i = 0; i < this.length(); i++) {
            if (this.encode(this.get(i)).equals(rawValue)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    public long length() {
        return this.jedis.llen(this.key);
    }

    /**
     * Taken from redisson 13th of March 2021, under the license "Apache License 2.0"
     * which allows for modification, commercial use, distribution and private use.
     */
    private long binarySearch(V value) {
        long size = this.length();
        long upperIndex = size - 1;
        long lowerIndex = 0;
        while (lowerIndex <= upperIndex) {
            long index = lowerIndex + (upperIndex - lowerIndex) / 2;
            V result = this.get(index);
            if (result == null) {
                return -1;
            }
            int comparableResult = result.compareTo(value);
            if (comparableResult == 0) {
                return index;
            } else if (comparableResult < 0) {
                upperIndex = index - 1;
            } else {
                lowerIndex = index + 1;
            }
        }
        return -(lowerIndex + 1);
    }
}
