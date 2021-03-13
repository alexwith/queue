package me.hyfe.queue.bootstrap;

import org.jetbrains.annotations.NotNull;

public interface BootstrapProvider {

    @NotNull
    Bootstrap bootstrap();

    void registerListeners();
}
