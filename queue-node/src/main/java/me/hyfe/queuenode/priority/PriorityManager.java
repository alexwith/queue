package me.hyfe.queuenode.priority;


import me.hyfe.helper.config.Config;
import me.hyfe.helper.config.ConfigController;
import me.hyfe.queuenode.priority.permissions.DefaultPermissionPredicate;
import me.hyfe.queuenode.priority.permissions.LuckPermsPermissionPredicate;
import me.hyfe.queuenode.priority.permissions.PermissionPredicate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.TreeSet;

public class PriorityManager {
    private final PermissionPredicate permissionPredicate;
    private final Set<Priority> priorities = new TreeSet<>();

    private static final int DEFAULT_PRIORITY = 0;

    public PriorityManager() {
        this.permissionPredicate = this.createPermissionPredicate();
    }

    public int getPriority(Player player) {
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

    private PermissionPredicate createPermissionPredicate() {
        return Bukkit.getPluginManager().isPluginEnabled("LuckPerms") ? new LuckPermsPermissionPredicate() : new DefaultPermissionPredicate();
    }
}
