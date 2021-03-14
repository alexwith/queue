package me.hyfe.queue.config;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ConfigController {
    private final Map<Class<? extends KeysHolder>, KeysHolder> keysHolders = new HashMap<>();
    private final Map<String, Config> configs = new HashMap<>();

    public Config get(String name) {
        return this.configs.get(name);
    }

    @SuppressWarnings("unchecked")
    public <T extends KeysHolder> T getKeysHolder(Class<T> keysHolder) {
        return (T) this.keysHolders.get(keysHolder);
    }

    public CompletableFuture<Void> reload(String name) {
        return this.configs.get(name).reload();
    }

    public CompletableFuture<Void> reloadAll() {
        return CompletableFuture.runAsync(() -> {
            for (Config config : this.configs.values()) {
                config.reload().join();
            }
        });
    }

    public void registerConfigs(KeysHolder... keyHolders) {
        for (KeysHolder keysHolder : keyHolders) {
            Config config = keysHolder.getConfig();
            this.keysHolders.put(keysHolder.getClass(), keysHolder);
            this.configs.put(config.getName(), config);
        }
    }
}