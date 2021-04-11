package me.hyfe.queuenode.priority;


import me.hyfe.helper.config.ConfigController;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.TreeSet;

public class PriorityManager {
    private final Set<Priority> priorities = new TreeSet<>();

    private static final int DEFAULT_PRIORITY = 0;

    public int getPriority(Player player) {
        for (Priority priority : this.priorities) {
            /*if (this.permissionPredicate.test(player, priority.getPermission())) {
                return priority.getPriority();
            }*/
        }
        return DEFAULT_PRIORITY;
    }

    public void load(ConfigController configController) {

    }
}
