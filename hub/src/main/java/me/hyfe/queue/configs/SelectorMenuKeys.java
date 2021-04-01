package me.hyfe.queue.configs;

import me.hyfe.helper.config.Config;
import me.hyfe.helper.config.KeysHolder;
import me.hyfe.helper.config.keys.ConfigKey;
import me.hyfe.helper.config.keys.ItemKey;
import me.hyfe.helper.menu.config.GuiItemsKey;

public class SelectorMenuKeys extends KeysHolder {

    public SelectorMenuKeys() {
        super(Config.create("selector-menu.yml", (path) -> path));
    }

    public static ConfigKey<String> TITLE = ConfigKey.of(SelectorMenuKeys.class, "title");
    public static ConfigKey<Integer> ROWS = ConfigKey.of(SelectorMenuKeys.class, "rows");
    public static ItemKey FILLER_ITEM = ItemKey.ofKey(SelectorMenuKeys.class, "filler-item");
}
