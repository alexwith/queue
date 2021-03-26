package me.hyfe.queue.managers;

import me.hyfe.queue.objects.Server;

import java.util.HashMap;
import java.util.Map;

public class ServerManager {
    private final Map<String, Server> servers = new HashMap<>();

    public ServerManager() {
        this.servers.put("dl", new Server("op.druglegends.net", 25565, "")); // testing
    }

    public Map<String, Server> getServers() {
        return this.servers;
    }
}
