package me.hyfe.queue.objects;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class Server {
    private final ZoneId releaseTimeZone;
    private final ZonedDateTime releaseDate;
    private final String ip;
    private final int port;
    private final String sendCommand;

    private Ping latestPing = new Ping(0, 0, false);

    public Server(String releaseDate, String releaseTime, String releaseTimeZone, String ip, int port, String sendCommand) {
        this.releaseTimeZone = ZoneId.of(releaseTimeZone);
        this.releaseDate = this.createDate(releaseDate, releaseTime);
        this.ip = ip;
        this.port = port;
        this.sendCommand = sendCommand;
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
