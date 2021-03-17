package me.hyfe.queue.menu;

import me.hyfe.helper.menu.gui.ConfigGui;
import me.hyfe.queue.configs.SelectorMenuKeys;
import org.bukkit.entity.Player;

public class SelectorMenu extends ConfigGui {

    public SelectorMenu(Player player) {
        super(player, SelectorMenuKeys.class);
    }

    @Override
    public void redraw() {

    }
}
