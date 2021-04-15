package me.hyfe.queuenode.priority.permissions;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;

import java.util.UUID;

public class LuckPermsPermissionPredicate implements PermissionPredicate {
    private final LuckPerms api = LuckPermsProvider.get();
    private final UserManager userManager = this.api.getUserManager();

    @Override
    public boolean test(UUID uuid, String permission) {
        User user = this.userManager.getUser(uuid);
        if (user == null) {
            return false;
        }
        return user.getCachedData().getPermissionData().checkPermission(permission).asBoolean();
    }
}
