package me.hyfe.queue.configs;

import me.hyfe.helper.config.Config;
import me.hyfe.helper.config.KeysHolder;
import me.hyfe.helper.config.keys.ConfigKey;
import me.hyfe.helper.config.keys.ItemKey;

public class ConfigKeys extends KeysHolder {

    public ConfigKeys() {
        super(Config.create("config.yml", (path) -> path));
    }

    public static final ConfigKey<Integer> PING_INTERVAL = ConfigKey.of(ConfigKeys.class, "ping-interval");
    public static final ItemKey COMPASS_ITEM = ItemKey.ofKey(ConfigKeys.class, "compass-item");
    public static final ConfigKey<Integer> COMPASS_SLOT = ConfigKey.of(ConfigKeys.class, "compass-item.slot");
}
