package me.hyfe.queue.config.holders;


import me.hyfe.queue.config.Config;
import me.hyfe.queue.config.KeysHolder;
import me.hyfe.queue.config.keys.ConfigKey;

public class RedisKeys extends KeysHolder {

    public RedisKeys() {
        super(Config.create("redis.yml", "queue", (path) -> path));
    }

    public static final ConfigKey<String> ADDRESS = ConfigKey.of(RedisKeys.class, "address");
    public static final ConfigKey<Integer> PORT = ConfigKey.of(RedisKeys.class, "port");
    public static final ConfigKey<String> PASSWORD = ConfigKey.of(RedisKeys.class, "password");
}
