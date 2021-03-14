package me.hyfe.queue.config.typekeys;

import me.hyfe.queue.bootstrap.Bootstrap;
import me.hyfe.queue.config.Config;
import me.hyfe.queue.config.KeysHolder;

import java.util.function.Supplier;

public class ConfigKey<T> implements Supplier<T> {
    private final Class<? extends KeysHolder> keysHolder;
    private final String key;

    public ConfigKey(Class<? extends KeysHolder> keysHolder, String key) {
        this.keysHolder = keysHolder;
        this.key = key;
    }

    public static <U> ConfigKey<U> of(Class<? extends KeysHolder> keysHolder, String key) {
        return new ConfigKey<>(keysHolder, key);
    }

    public Config getConfig() {
        return Bootstrap.get().getConfigController().getKeysHolder(this.keysHolder).getConfig();
    }

    public String getKey() {
        return this.key;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get() {
        return (T) this.getConfig().get(this.key);
    }
}
