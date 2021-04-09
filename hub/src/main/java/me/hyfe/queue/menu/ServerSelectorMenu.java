package me.hyfe.queue.menu;

import me.hyfe.helper.Schedulers;
import me.hyfe.helper.menu.gui.Gui;
import me.hyfe.helper.menu.item.Item;
import me.hyfe.queue.managers.ServerManager;
import me.hyfe.queue.objects.Category;
import me.hyfe.queue.objects.Ping;
import me.hyfe.queue.objects.Server;
import me.hyfe.queue.utils.TimeUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.TimeUnit;

public class ServerSelectorMenu extends Gui {
    private final Plugin plugin;
    private final ServerManager serverManager;

    public ServerSelectorMenu(Player player, String title, int rows, Plugin plugin, ServerManager serverManager) {
        super(player, title, rows);
        this.plugin = plugin;
        this.serverManager = serverManager;
    }

    @Override
    public void redraw() {
        if (this.firstDraw) {
            for (Category category : this.serverManager.getCategories()) {
                Item item = Item.builder(category.getItem())
                        .bind(() -> {
                            this.close();
                            Schedulers.sync().run(() -> {
                                CategoryMenu categoryMenu = new CategoryMenu(this.player, this.plugin, this.serverManager, category);
                                categoryMenu.open();
                            });
                        }, ClickType.RIGHT, ClickType.LEFT)
                        .build();
                this.setItem(item, category.getSlot());
            }
        }
        for (Server server : this.serverManager.getServers("none")) {
            Ping ping = server.getLatestPing();
            server.applyItem(this.plugin, this.player, this, ping.isOnline(), replacer -> replacer
                    .set("online_players", ping.getOnlinePlayers())
                    .set("max_players", ping.getMaxPlayers())
                    .set("age", TimeUtil.format(TimeUnit.SECONDS, server.time()))
            );
        }
        this.serverManager.applyFiller(this);
    }
}
