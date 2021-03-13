package me.hyfe.queue.bootstrap;

import me.hyfe.queue.proxy.ProxyDelegate;
import me.hyfe.queue.queue.QueueManager;
import org.jetbrains.annotations.NotNull;

public interface BootstrapProvider<T, U> {

    @NotNull
    Bootstrap bootstrap();

    @NotNull
    QueueManager<T, U> createQueueManager();

    @NotNull
    ProxyDelegate<T> getProxyDelegate();

    void registerListeners();
}
