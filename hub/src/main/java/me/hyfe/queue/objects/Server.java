package me.hyfe.queue.objects;

import me.hyfe.helper.menu.gui.Gui;
import me.hyfe.helper.menu.item.Item;
import me.hyfe.helper.text.replacer.Replacer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.function.Function;

public class Server {
    private final String name;
    private final String category;
    private final ZoneId releaseTimeZone;
    private final ZonedDateTime releaseDate;
    private final String ip;
    private final int port;
    private final String sendCommand;
    private final int slot;
    private final ItemStack onlineItem;
    private final ItemStack offlineItem;

    private Ping latestPing = new Ping(0, 0, false);

    public Server(String name, String category, String releaseTimeZone, String releaseDate, String releaseTime, String ip, int port, String sendCommand, int slot, ItemStack onlineItem, ItemStack offlineItem) {
        this.name = name;
        this.category = category;
        this.releaseTimeZone = ZoneId.of(releaseTimeZone);
        this.releaseDate = this.createDate(releaseDate, releaseTime);
        this.ip = ip;
        this.port = port;
        this.sendCommand = sendCommand;
        this.slot = slot;
        this.onlineItem = onlineItem;
        this.offlineItem = offlineItem;
    }

    public String getName() {
        return this.name;
    }

    public String getCategory() {
        return this.category;
    }

    public Ping getLatestPing() {
        return this.latestPing;
    }

    public void ping() {
        try {
            this.latestPing = Ping.generate(this.ip, this.port);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public long time() {
        return Math.abs(ZonedDateTime.now(this.releaseTimeZone).until(this.releaseDate, ChronoUnit.SECONDS));
    }

    public void send(Player player) {
        Bukkit.dispatchCommand(player, this.sendCommand);
    }

    public void applyItem(Player player, Gui gui, boolean online, Replacer replacer) {
        Item item = Item.builder(online ? this.onlineItem : this.offlineItem)
                .bind(() -> {
                    this.send(player);
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
