package me.hyfe.queue.redis;

import org.jetbrains.annotations.NotNull;

public interface RedisProvider {

    @NotNull
    Redis getRedis();
}
