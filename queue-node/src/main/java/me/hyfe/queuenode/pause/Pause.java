package me.hyfe.queuenode.pause;

public class Pause {
    private final boolean paused;
    private final PauseReason reason;

    public Pause(boolean isPaused, PauseReason reason) {
        this.paused = isPaused;
        this.reason = reason;
    }

    public boolean isPaused() {
        return this.paused;
    }

    public PauseReason getReason() {
        return this.reason;
    }
}
