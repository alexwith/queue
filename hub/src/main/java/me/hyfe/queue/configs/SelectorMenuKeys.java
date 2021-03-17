package me.hyfe.queue.configs;

import me.hyfe.helper.config.Config;
import me.hyfe.helper.config.KeysHolder;
import me.hyfe.helper.config.keys.ConfigKey;

public class SelectorMenuKeys extends KeysHolder {

    public SelectorMenuKeys() {
        super(Config.create("selector-menu.yml", "hub", (path) -> path));
    }

    public static ConfigKey<String> TITLE = ConfigKey.of(SelectorMenuKeys.class, "title");
    public static ConfigKey<Integer> ROWS = ConfigKey.of(SelectorMenuKeys.class, "rows");
}
