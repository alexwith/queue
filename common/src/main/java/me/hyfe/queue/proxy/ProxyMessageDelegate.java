package me.hyfe.queue.proxy;

import me.hyfe.queue.text.Text;
import me.hyfe.queue.text.replacer.Replacer;

public abstract class ProxyMessageDelegate<T> {

    public abstract void messageDelegate(T player, String message);

    public void sendMessage(T player, String message) {
        this.sendMessage(player, message, null);
    }

    public void sendMessage(T player, String message, Replacer replacer) {
        this.messageDelegate(player, Text.colorize(message, replacer));
    }
}
