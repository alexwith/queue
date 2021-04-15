package me.hyfe.queuenode.priority.permissions;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import org.bukkit.entity.Player;

public class LuckPermsPermissionPredicate implements PermissionPredicate {
    private final LuckPerms api = LuckPermsProvider.get();
    private final UserManager userManager = this.api.getUserManager();

    @Override
    public boolean test(Player player, String permission) {
        User user = this.userManager.getUser(player.getUniqueId());
        if (user == null) {
            return false;
        }
        return user.getCachedData().getPermissionData().checkPermission(permission).asBoolean();
    }
}
