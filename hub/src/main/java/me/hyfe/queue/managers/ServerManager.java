package me.hyfe.queue.managers;

import me.hyfe.helper.config.Config;
import me.hyfe.helper.plugin.HelperPlugin;
import me.hyfe.queue.objects.Server;

import java.util.HashMap;
import java.util.Map;

public class ServerManager {
    private final Map<String, Server> servers = new HashMap<>();

    public ServerManager(HelperPlugin plugin) {
        this.load(plugin);
    }

    public Map<String, Server> getServers() {
        return this.servers;
    }

    public Server getServer(String key) {
        return this.servers.get(key);
    }

    private void load(HelperPlugin plugin) {
        Config config = plugin.getConfig("config.yml");
        for (String key : config.getKeys("servers")) {
            String ip = config.tryGet("servers." + key + ".ping.ip");
            int port = config.tryGet("servers." + key + ".ping.port");
            String releaseDate = config.tryGet("servers." + key + ".release.date");
            String releaseTime = config.tryGet("servers." + key + ".release.time");
            String releaseTimeZone = config.tryGet("servers." + key + ".release.time-zone");
            String sendCommand = config.tryGet("servers." + key + ".send-command");
            Server server = new Server(releaseDate, releaseTime, releaseTimeZone, ip, port, sendCommand);
            this.servers.put(key, server);
        }
    }
}
