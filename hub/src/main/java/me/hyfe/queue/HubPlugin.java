package me.hyfe.queue;

import me.hyfe.helper.Events;
import me.hyfe.helper.plugin.HelperPlugin;
import me.hyfe.queue.commands.QueueCommand;
import me.hyfe.queue.configs.ConfigKeys;
import me.hyfe.queue.configs.SelectorMenuKeys;
import me.hyfe.queue.menu.SelectorMenu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;

public class HubPlugin extends HelperPlugin {
    private final BiConsumer<Player, String> serverSender = (player, server) -> Bukkit.dispatchCommand(player, "server ".concat(server));

    @Override
    protected void enable() {
        this.configController.registerConfigs(
                new ConfigKeys(),
                new SelectorMenuKeys()
        );
        this.commands();
        this.serverSelectorListeners();
    }

    @Override
    protected void disable() {

    }

    public BiConsumer<Player, String> getServerSender() {
        return this.serverSender;
    }

    private void commands() {
        new QueueCommand(this);
    }

    private void serverSelectorListeners() {
        Events.subscribe(PlayerJoinEvent.class)
                .handler((event) -> {

                });
        Events.subscribe(PlayerInteractEvent.class)
                .handler((event) -> {
                    Player player = event.getPlayer();
                    ItemStack itemStack = event.getItem();
                    if (itemStack == null || !itemStack.getType().equals(Material.COMPASS)) {
                        return;
                    }
                    SelectorMenu selectorMenu = new SelectorMenu(this, player);
                    selectorMenu.open();
                });
    }
}
