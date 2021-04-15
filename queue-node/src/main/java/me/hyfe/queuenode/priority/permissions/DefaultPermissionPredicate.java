package me.hyfe.queuenode.priority.permissions;

import org.bukkit.entity.Player;

public class DefaultPermissionPredicate implements PermissionPredicate {

    @Override
    public boolean test(Player player, String permission) {
        return player.hasPermission(permission);
    }
}
