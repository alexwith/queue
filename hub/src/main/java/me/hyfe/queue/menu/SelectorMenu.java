package me.hyfe.queue.menu;

import me.hyfe.helper.config.Config;
import me.hyfe.helper.item.ItemStackBuilder;
import me.hyfe.helper.menu.config.ConfigGui;
import me.hyfe.helper.menu.item.Item;
import me.hyfe.queue.HubPlugin;
import me.hyfe.queue.configs.SelectorMenuKeys;
import me.hyfe.queue.managers.ServerManager;
import me.hyfe.queue.objects.Ping;
import me.hyfe.queue.objects.Server;
import me.hyfe.queue.utils.TimeUtil;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class SelectorMenu extends ConfigGui {
    private final ServerManager serverManager;
    private final Config config;

    public SelectorMenu(HubPlugin plugin, Player player) {
        super(player, SelectorMenuKeys.class);
        this.serverManager = plugin.getServerManager();
        this.config = plugin.getConfig("selector-menu.yml");
    }

    @Override
    public void redraw() {
        if (this.firstDraw) {
            for (String key : this.config.getKeys("servers")) {
                Server server = this.serverManager.getServer(key);
                if (server == null) {
                    continue;
                }
                Ping ping = server.getLatestPing();
                key = key + "." + (ping.isOnline() ? "online" : "offline");
                Item.Builder builder = Item.builder(ItemStackBuilder.of(this.config, "servers." + key + ".item").build());
                Item item = builder.build(replacer -> replacer
                        .set("online_players", ping.getOnlinePlayers())
                        .set("max_players", ping.getMaxPlayers())
                        .set("age", TimeUtil.format(TimeUnit.SECONDS, server.time())));
                int slot = this.config.tryGet("servers." + key + ".slot");
                this.setItem(item, slot);
            }
        }
    }
}
