package me.hyfe.queue.menu;

import me.hyfe.helper.menu.gui.Gui;
import me.hyfe.queue.managers.ServerManager;
import me.hyfe.queue.objects.Category;
import me.hyfe.queue.objects.Ping;
import me.hyfe.queue.objects.Server;
import me.hyfe.queue.utils.TimeUtil;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class CategoryMenu extends Gui {
    private final ServerManager serverManager;
    private final Category category;

    public CategoryMenu(Player player, ServerManager serverManager, Category category) {
        super(player, category.getTitle(), category.getRows());
        this.setFallback((ignore) -> serverManager.createServerSelectorMenu(player));
        this.serverManager = serverManager;
        this.category = category;
    }

    @Override
    public void redraw() {
        if (this.firstDraw) {
            for (Server server : this.serverManager.getServers(this.category.getName())) {
                Ping ping = server.getLatestPing();
                server.applyItem(this.player, this, ping.isOnline(), replacer -> replacer
                        .set("online_players", ping.getOnlinePlayers())
                        .set("max_players", ping.getMaxPlayers())
                        .set("age", TimeUtil.format(TimeUnit.SECONDS, server.time()))
                );
            }
            this.serverManager.applyFiller(this);
        }
    }
}
