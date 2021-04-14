package me.hyfe.queue.objects;

import me.hyfe.helper.Schedulers;
import me.hyfe.helper.menu.gui.Gui;
import me.hyfe.helper.menu.item.Item;
import me.hyfe.helper.text.replacer.Replacer;
import me.hyfe.queue.redis.Redis;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class Server {
    private final String id;
    private final String nodeId;
    private final ZoneId releaseTimeZone;
    private final ZonedDateTime releaseDate;
    private final int slot;
    private final ItemStack onlineItem;
    private final ItemStack offlineItem;
    private final ItemStack whitelistedItem;

    private Ping latestPing = new Ping(0, 0, false, false);

    public Server(String id, String nodeId, String releaseTimeZone, String releaseDate, String releaseTime, int slot, ItemStack onlineItem, ItemStack offlineItem, ItemStack whitelistedItem) {
        this.id = id;
        this.nodeId = nodeId;
        this.releaseTimeZone = ZoneId.of(releaseTimeZone);
        this.releaseDate = this.createDate(releaseDate, releaseTime);
        this.slot = slot;
        this.onlineItem = onlineItem;
        this.offlineItem = offlineItem;
        this.whitelistedItem = whitelistedItem;
    }

    public String getId() {
        return this.id;
    }

    public Ping getLatestPing() {
        return this.latestPing;
    }

    public void ping(Redis redis) {
        try {
            this.latestPing = Ping.generate(redis, this.nodeId);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public long time() {
        return Math.abs(ZonedDateTime.now(this.releaseTimeZone).until(this.releaseDate, ChronoUnit.SECONDS));
    }

    public void applyItem(Player player, Gui gui, Ping ping, Replacer replacer) {
        Item item = Item.builder(ping.isOnline() ? ping.isWhitelisted() ? this.whitelistedItem : this.onlineItem : this.offlineItem)
                .bind(() -> {
                    Bukkit.dispatchCommand(player, "queuejoin ".concat(this.nodeId));
                    player.closeInventory();
                    Schedulers.sync().runLater(player::closeInventory, 3); // compensate for fallback gui
                }, ClickType.RIGHT, ClickType.LEFT)
                .build(replacer);
        gui.setItem(item, this.slot);
    }

    private ZonedDateTime createDate(String date, String time) {
        String[] dateArgs = date.split("/");
        String[] timeArgs = time.split(":");
        int year = Integer.parseInt(dateArgs[2]);
        int month = Integer.parseInt(dateArgs[1]);
        int day = Integer.parseInt(dateArgs[0]);
        int hour = Integer.parseInt(timeArgs[0]);
        int minute = Integer.parseInt(timeArgs[1]);
        return ZonedDateTime.of(year, month, day, hour, minute, 0, 0, this.releaseTimeZone);
    }
}
