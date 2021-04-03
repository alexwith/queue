package me.hyfe.queue;

import me.hyfe.helper.Events;
import me.hyfe.helper.config.Config;
import me.hyfe.helper.plugin.HelperPlugin;
import me.hyfe.queue.commands.QueueCommand;
import me.hyfe.queue.configs.ConfigKeys;
import me.hyfe.queue.managers.ServerManager;
import me.hyfe.queue.task.PingTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;

public class HubPlugin extends HelperPlugin {
    private ServerManager serverManager;
    private PingTask pingTask;

    private final BiConsumer<Player, String> serverSender = (player, server) -> Bukkit.dispatchCommand(player, "server ".concat(server));

    @Override
    protected void enable() {
        this.configController.registerConfigs(
                new ConfigKeys()
        );
        this.configController.registerConfigs(
                Config.create("servers.yml", (path) -> path)
        );
        this.serverManager = new ServerManager(this);
        this.pingTask = new PingTask(this.serverManager);
        this.commands();
        this.commonListeners();
        this.serverSelectorListeners();
    }

    @Override
    protected void disable() {
        this.pingTask.close();
    }

    public ServerManager getServerManager() {
        return this.serverManager;
    }

    public BiConsumer<Player, String> getServerSender() {
        return this.serverSender;
    }

    private void commands() {
        new QueueCommand(this);
    }

    private void serverSelectorListeners() {
        ItemStack compassItem = ConfigKeys.COMPASS_ITEM.get();
        int compassSlot = ConfigKeys.COMPASS_SLOT.get();
        Events.subscribe(PlayerJoinEvent.class)
                .handler((event) -> {
                    Player player = event.getPlayer();
                    Inventory inventory = player.getInventory();
                    inventory.setItem(compassSlot, compassItem);
                });
        Events.subscribe(PlayerInteractEvent.class)
                .handler((event) -> {
                    Player player = event.getPlayer();
                    ItemStack itemStack = event.getItem();
                    if (itemStack == null || !itemStack.getType().equals(compassItem.getType())) {
                        return;
                    }
                    this.serverManager.createServerSelectorMenu(player).open();
                });
    }

    private void commonListeners() {
        Events.subscribe(PlayerDropItemEvent.class)
                .handler((event) -> {
                    event.getItemDrop().remove();
                    event.setCancelled(true);
                });
        Events.subscribe(InventoryClickEvent.class)
                .handler((event) -> {
                    event.setCancelled(true);
                });
        Events.subscribe(BlockBreakEvent.class)
                .handler((event) -> {
                    event.setCancelled(true);
                });
        Events.subscribe(PlayerInteractEvent.class)
                .handler((event) -> {
                    event.setCancelled(true);
                });
    }
}
