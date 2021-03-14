package me.hyfe.queue.proxy.delegates;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface ProxyDelegate<T> {

    @Nullable
    T getPlayer(@NotNull UUID uuid);
}
