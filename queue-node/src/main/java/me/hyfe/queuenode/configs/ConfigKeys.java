package me.hyfe.queuenode.configs;

import me.hyfe.helper.config.Config;
import me.hyfe.helper.config.KeysHolder;
import me.hyfe.helper.config.keys.ConfigKey;

import java.util.List;

public class ConfigKeys extends KeysHolder {

    public ConfigKeys() {
        super(Config.create("config.yml", (path) -> path));
    }

    public static final ConfigKey<String> NODE_IDENTIFIER = ConfigKey.of(ConfigKeys.class, "node-identifier");
    public static final ConfigKey<Boolean> IS_HUB = ConfigKey.of(ConfigKeys.class, "hub");
    public static final ConfigKey<List<String>> NON_HUB_NODES = ConfigKey.of(ConfigKeys.class, "non-hub-nodes");
    public static final ConfigKey<Integer> POLL_INTERVAL = ConfigKey.of(ConfigKeys.class, "poll-interval");
    public static final ConfigKey<Integer> POSITION_INTERVAL = ConfigKey.of(ConfigKeys.class, "position-interval");
    public static final ConfigKey<String> BYPASS_PERMISSION = ConfigKey.of(ConfigKeys.class, "bypass-permission");
}
