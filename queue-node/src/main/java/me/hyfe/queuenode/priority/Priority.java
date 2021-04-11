package me.hyfe.queuenode.priority;

import org.jetbrains.annotations.NotNull;

public class Priority implements Comparable<Priority> {
    private final String permission;
    private final int priority;

    public Priority(String permission, int priority) {
        this.permission = permission;
        this.priority = priority;
    }

    public String getPermission() {
        return this.permission;
    }

    public int getPriority() {
        return this.priority;
    }

    @Override
    public int compareTo(@NotNull Priority priority) {
        return Integer.compare(this.priority, priority.priority);
    }
}
