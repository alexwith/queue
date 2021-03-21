package me.hyfe.queue.priorities;

import me.hyfe.queue.config.Config;
import me.hyfe.queue.config.ConfigController;

import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiPredicate;

public class PriorityManager<T> {
    private final Set<Priority> priorities = new TreeSet<>();
    private final BiPredicate<T, String> permissionPredicate;

    private static final int DEFAULT_PRIORITY = 0;

    public PriorityManager(BiPredicate<T, String> permissionPredicate) {
        this.permissionPredicate = permissionPredicate;
    }

    public int getPriority(T player) {
        for (Priority priority : this.priorities) {
            if (this.permissionPredicate.test(player, priority.getPermission())) {
                return priority.getPriority();
            }
        }
        return DEFAULT_PRIORITY;
    }

    public void load(ConfigController configController) {
        Config config = configController.get("config.yml");
        for (String key : config.getKeys("priorities")) {
            String permission = config.tryGet("priorities." + key + ".permission");
            int priority = config.tryGet("priorities." + key + ".priority");
            this.priorities.add(new Priority(permission, priority));
        }
    }
}
