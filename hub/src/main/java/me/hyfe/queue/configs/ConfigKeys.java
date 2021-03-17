package me.hyfe.queue.configs;

import me.hyfe.helper.config.Config;
import me.hyfe.helper.config.KeysHolder;

public class ConfigKeys extends KeysHolder {

    public ConfigKeys() {
        super(Config.create("config.yml", "hub", (path) -> path));
    }
}
