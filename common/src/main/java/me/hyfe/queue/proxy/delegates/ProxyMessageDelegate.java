package me.hyfe.queue.proxy.delegates;

import me.hyfe.queue.text.Text;
import me.hyfe.queue.text.replacer.Replacer;

public interface ProxyMessageDelegate<T> {

    void messageDelegate(T player, String message);

    default void sendMessage(T player, String message) {
        this.sendMessage(player, message, null);
    }

    default void sendMessage(T player, String message, Replacer replacer) {
        this.messageDelegate(player, Text.colorize(message, replacer));
    }
}
