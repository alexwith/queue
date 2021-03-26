package me.hyfe.queue;

import me.hyfe.helper.Events;
import me.hyfe.helper.plugin.HelperPlugin;
import me.hyfe.queue.commands.QueueCommand;
import me.hyfe.queue.configs.ConfigKeys;
import me.hyfe.queue.configs.SelectorMenuKeys;
import me.hyfe.queue.managers.ServerManager;
import me.hyfe.queue.menu.SelectorMenu;
import me.hyfe.queue.objects.Ping;
import me.hyfe.queue.objects.Server;
import me.hyfe.queue.task.PingTask;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
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
                new ConfigKeys(),
                new SelectorMenuKeys()
        );
        this.serverManager = new ServerManager();
        this.pingTask = new PingTask(this.serverManager);
        this.commands();
        this.commonListeners();
        this.serverSelectorListeners();
    }

    @Override
    protected void disable() {
        this.pingTask.close();
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
                    if (itemStack == null || !itemStack.getType().equals(Material.COMPASS)) {
                        return;
                    }
                    SelectorMenu selectorMenu = new SelectorMenu(this, player);
                    selectorMenu.open();

                    Server server = this.serverManager.getServers().get("dl");
                    Ping ping = server.getLatestPing();
                    System.out.println("isOnline: " + ping.isOnline() + ", online: " + ping.getOnlinePlayers() + ", max: " + ping.getMaxPlayers());
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
    }
}
