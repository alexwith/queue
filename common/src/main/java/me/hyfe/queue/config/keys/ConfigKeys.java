package me.hyfe.queue.config.keys;

import me.hyfe.queue.config.Config;
import me.hyfe.queue.config.KeysHolder;
import me.hyfe.queue.config.typekeys.ConfigKey;

import java.util.List;

public class ConfigKeys extends KeysHolder {

    public ConfigKeys() {
        super(Config.create("config.yml", "queue", (path) -> path));
    }

    public static final ConfigKey<Integer> POLL_INTERVAL = ConfigKey.of(ConfigKeys.class, "poll-interval");
    public static final ConfigKey<Integer> POSITION_INTERVAL = ConfigKey.of(ConfigKeys.class, "position-interval");
    public static final ConfigKey<List<String>> HUBS = ConfigKey.of(ConfigKeys.class, "hubs");
}
