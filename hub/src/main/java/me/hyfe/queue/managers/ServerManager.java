package me.hyfe.queue.managers;

import me.hyfe.helper.config.Config;
import me.hyfe.helper.item.ItemStackBuilder;
import me.hyfe.helper.menu.gui.Gui;
import me.hyfe.helper.menu.item.Item;
import me.hyfe.helper.plugin.HelperPlugin;
import me.hyfe.queue.menu.ServerSelectorMenu;
import me.hyfe.queue.objects.Category;
import me.hyfe.queue.objects.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.UnaryOperator;

public class ServerManager {
    private final HelperPlugin plugin;
    private final Map<String, Category> categories = new HashMap<>();
    private final Map<String, Server> servers = new HashMap<>();
    private final Map<String, Set<Server>> categoryRelations = new HashMap<>();

    private final Item fillerItem;

    public ServerManager(HelperPlugin plugin) {
        this.plugin = plugin;
        this.fillerItem = this.getFillerItem();
        this.loadCategories();
        this.loadServers();
    }

    public Collection<Server> getServers() {
        return this.servers.values();
    }

    public Collection<Category> getCategories() {
        return this.categories.values();
    }

    public Set<Server> getServers(String category) {
        return this.categoryRelations.getOrDefault(category, new HashSet<>());
    }

    public ServerSelectorMenu createServerSelectorMenu(Player player) {
        Config config = this.plugin.getConfig("servers.yml");
        return new ServerSelectorMenu(player, config.tryGet("main-menu.title"), config.tryGet("main-menu.rows"), this.plugin, this);
    }

    public void applyFiller(Gui gui) {
        while (gui.firstEmpty() != -1) {
            gui.addItem(this.fillerItem);
        }
    }

    private Item getFillerItem() {
        Config config = this.plugin.getConfig("servers.yml");
        return Item.builder(ItemStackBuilder.of(config, "filler").build()).build();
    }

    private void loadCategories() {
        Config config = this.plugin.getConfig("servers.yml");
        for (String key : config.getKeys("categories")) {
            UnaryOperator<String> path = (extension) -> "categories." + key + "." + extension;
            String title = config.tryGet(path.apply("title"));
            int rows = config.tryGet(path.apply("rows"));
            int slot = config.tryGet(path.apply("slot"));
            ItemStack item = ItemStackBuilder.of(config, path.apply("item")).build();
            Category category = new Category(key, title, rows, slot, item);
            this.categories.put(key, category);
        }
    }

    private void loadServers() {
        Config config = this.plugin.getConfig("servers.yml");
        for (String key : config.getKeys("servers")) {
            UnaryOperator<String> path = (extension) -> "servers." + key + "." + extension;
            String nodeId = config.tryGet(path.apply("node-identifier"));
            String category = config.tryGet(path.apply("category"));
            String releaseDate = config.tryGet(path.apply("release.date"));
            String releaseTime = config.tryGet(path.apply("release.time"));
            String releaseTimeZone = config.tryGet(path.apply("release.time-zone"));
            int slot = config.tryGet(path.apply("slot"));
            ItemStack onlineItem = ItemStackBuilder.of(config, path.apply("online-item")).build();
            ItemStack offlineItem = ItemStackBuilder.of(config, path.apply("offline-item")).build();
            ItemStack whitelistedItem = ItemStackBuilder.of(config, path.apply("whitelisted-item")).build();
            Server server = new Server(key, nodeId, releaseTimeZone, releaseDate, releaseTime, slot, onlineItem, offlineItem, whitelistedItem);
            this.servers.put(key, server);
            if (!this.categoryRelations.containsKey(category)) {
                this.categoryRelations.put(category, new HashSet<>());
            }
            this.categoryRelations.get(category).add(server);
        }
    }
}
