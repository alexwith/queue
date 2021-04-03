package me.hyfe.queue.objects;

import me.hyfe.helper.text.Text;
import org.bukkit.inventory.ItemStack;

public class Category {
    private final String name;
    private final String title;
    private final int rows;
    private final int slot;
    private final ItemStack item;

    public Category(String name, String title, int rows, int slot, ItemStack item) {
        this.name = name;
        this.title = title;
        this.rows = rows;
        this.slot = slot;
        this.item = item;
    }

    public String getName() {
        return this.name;
    }

    public String getTitle() {
        return Text.colorize(this.title);
    }

    public int getRows() {
        return this.rows;
    }

    public int getSlot() {
        return this.slot;
    }

    public ItemStack getItem() {
        return this.item;
    }
}
