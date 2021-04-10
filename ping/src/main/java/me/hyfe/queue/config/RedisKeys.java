package me.hyfe.queue.config;


import me.hyfe.helper.config.Config;
import me.hyfe.helper.config.KeysHolder;
import me.hyfe.helper.config.keys.ConfigKey;

public class RedisKeys extends KeysHolder {

    public RedisKeys() {
        super(Config.create("redis.yml", (path) -> path));
    }

    public static final ConfigKey<String> ADDRESS = ConfigKey.of(RedisKeys.class, "address");
    public static final ConfigKey<Integer> PORT = ConfigKey.of(RedisKeys.class, "port");
    public static final ConfigKey<String> PASSWORD = ConfigKey.of(RedisKeys.class, "password");
}
