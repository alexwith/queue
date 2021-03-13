package me.hyfe.queue.proxy;

import java.util.function.Consumer;

public abstract class ServerSender<T, U> implements Consumer<T> {
    protected final U target;

    public ServerSender(U target) {
        this.target = target;
    }
}
