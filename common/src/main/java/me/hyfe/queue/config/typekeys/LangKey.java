package me.hyfe.queue.config.typekeys;

import me.hyfe.queue.config.KeysHolder;
import me.hyfe.queue.proxy.delegates.ProxyMessageDelegate;
import me.hyfe.queue.text.replacer.Replacer;

public class LangKey extends ConfigKey<String> {

    public LangKey(Class<? extends KeysHolder> keysHolder, String key) {
        super(keysHolder, key);
    }

    public static LangKey ofKey(Class<? extends KeysHolder> keysHolder, String key) {
        return new LangKey(keysHolder, key);
    }

    public <T> void send(T player, ProxyMessageDelegate<T> messageDelegate) {
        this.send(player, messageDelegate, null);
    }

    public <T> void send(T player, ProxyMessageDelegate<T> messageDelegate, Replacer replacer) {
        messageDelegate.sendMessage(player, this.get(), replacer);
    }
}
