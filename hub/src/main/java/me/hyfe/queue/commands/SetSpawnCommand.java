package me.hyfe.queue.commands;

import me.hyfe.helper.Commands;
import me.hyfe.queue.HubPlugin;
import org.bukkit.Location;

public class SetSpawnCommand {
    private final HubPlugin plugin;

    public SetSpawnCommand(HubPlugin plugin) {
        this.plugin = plugin;
        this.createCommand();
    }

    private void createCommand() {
        Commands.create("setspawn")
                .player()
                .permission("queue.admin")
                .handler((sender, context) -> {
                    Location location = sender.getLocation();
                    this.plugin.setSpawnPoint(location);
                }).register();
    }
}
