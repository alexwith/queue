package me.hyfe.queue.bootstrap;

import me.hyfe.queue.proxy.ProxyDelegate;
import me.hyfe.queue.queue.QueueManager;
import org.jetbrains.annotations.NotNull;

public interface BootstrapProvider<T> {

    @NotNull
    Bootstrap bootstrap();

    @NotNull
    QueueManager<T> createQueueManager();

    @NotNull
    ProxyDelegate<T> getProxyDelegate();

    void registerListeners();
}
