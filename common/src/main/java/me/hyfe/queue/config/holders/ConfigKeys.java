package me.hyfe.queue.config.holders;

import me.hyfe.queue.config.Config;
import me.hyfe.queue.config.KeysHolder;
import me.hyfe.queue.config.keys.ConfigKey;

public class ConfigKeys extends KeysHolder {

    public ConfigKeys() {
        super(Config.create("config.yml", "queue", (path) -> path));
    }

    public static final ConfigKey<Long> POLL_INTERVAL = ConfigKey.of(ConfigKeys.class, "poll-interval");
}
