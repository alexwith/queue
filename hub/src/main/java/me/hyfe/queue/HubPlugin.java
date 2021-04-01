package me.hyfe.queue;

import com.google.gson.Gson;
import me.hyfe.helper.Events;
import me.hyfe.helper.plugin.HelperPlugin;
import me.hyfe.queue.commands.QueueCommand;
import me.hyfe.queue.commands.SetSpawnCommand;
import me.hyfe.queue.configs.ConfigKeys;
import me.hyfe.queue.configs.SelectorMenuKeys;
import me.hyfe.queue.managers.ServerManager;
import me.hyfe.queue.menu.SelectorMenu;
import me.hyfe.queue.task.PingTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.BiConsumer;

public class HubPlugin extends HelperPlugin {
    private ServerManager serverManager;
    private PingTask pingTask;
    private Location spawnPoint;

    private final BiConsumer<Player, String> serverSender = (player, server) -> Bukkit.dispatchCommand(player, "server ".concat(server));

    @Override
    protected void enable() {
        this.configController.registerConfigs(
                new ConfigKeys(),
                new SelectorMenuKeys()
        );
        this.serverManager = new ServerManager(this);
        this.pingTask = new PingTask(this.serverManager);
        this.loadSpawnPoint();
        this.commands();
        this.commonListeners();
        this.serverSelectorListeners();
    }

    @Override
    protected void disable() {
        this.pingTask.close();
        this.saveSpawnPoint();
    }

    public ServerManager getServerManager() {
        return this.serverManager;
    }

    public Location getSpawnPoint() {
        return this.spawnPoint;
    }

    public void setSpawnPoint(Location spawnPoint) {
        this.spawnPoint = spawnPoint;
    }

    public BiConsumer<Player, String> getServerSender() {
        return this.serverSender;
    }

    private void commands() {
        new QueueCommand(this);
        new SetSpawnCommand(this);
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
                });
    }

    private void commonListeners() {
        Events.subscribe(PlayerJoinEvent.class)
                .handler((event) -> {
                    Player player = event.getPlayer();
                    player.teleport(this.spawnPoint);
                });
        Events.subscribe(PlayerRespawnEvent.class)
                .handler((event) -> {
                    Player player = event.getPlayer();
                    player.teleport(this.spawnPoint);
                });
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

    private void loadSpawnPoint() {
        Gson gson = new Gson();
        try {
            File file = this.getDataFolder().toPath().resolve("spawn-point.json").toFile();
            FileReader reader = new FileReader(file);
            this.spawnPoint = gson.fromJson(reader, Location.class);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void saveSpawnPoint() {
        Gson gson = new Gson();
        try {
            Path path = this.getDataFolder().toPath().resolve("spawn-point.json");
            Writer writer = Files.newBufferedWriter(path);
            gson.toJson(this.spawnPoint, writer);
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
