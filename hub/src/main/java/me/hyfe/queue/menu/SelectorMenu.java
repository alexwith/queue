package me.hyfe.queue.menu;

import me.hyfe.helper.config.Config;
import me.hyfe.helper.menu.config.ConfigGui;
import me.hyfe.queue.HubPlugin;
import me.hyfe.queue.configs.SelectorMenuKeys;
import org.bukkit.entity.Player;

public class SelectorMenu extends ConfigGui {
    private final Config config;

    public SelectorMenu(HubPlugin plugin, Player player) {
        super(player, SelectorMenuKeys.class);
        this.config = plugin.getConfig("selector-menu.yml");
    }

    @Override
    public void redraw() {
        if (this.firstDraw) {
            SelectorMenuKeys.SERVERS.toGui(this, (builder) -> builder);
        }
    }
}
