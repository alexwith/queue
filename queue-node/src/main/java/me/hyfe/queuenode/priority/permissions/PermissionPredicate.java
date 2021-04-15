package me.hyfe.queuenode.priority.permissions;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface PermissionPredicate {

    boolean test(Player player, String permission);
}
