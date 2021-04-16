package me.hyfe.queue.objects;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.hyfe.queue.redis.Redis;

import java.io.IOException;

public class Ping {
    private final int onlinePlayers;
    private final int maxPlayers;
    private final boolean whitelisted;
    private final boolean online;

    private static final JsonParser PARSER = new JsonParser();

    public Ping(int onlinePlayers, int maxPlayers, boolean whitelisted, boolean online) {
        this.onlinePlayers = onlinePlayers;
        this.maxPlayers = maxPlayers;
        this.whitelisted = whitelisted;
        this.online = online;
    }

    public int getOnlinePlayers() {
        return this.onlinePlayers;
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    public boolean isWhitelisted() {
        return this.whitelisted;
    }

    public boolean isOnline() {
        return this.online;
    }

    public static Ping generate(Redis redis, String id) throws IOException {
        String finalId = id.concat("-status").toLowerCase();
        return redis.provideJedis((jedis) -> {
            if (!jedis.exists(finalId)) {
                return new Ping(0, 0, false, false);
            }
            String jsonString = jedis.get(finalId);
            JsonObject json = PARSER.parse(jsonString).getAsJsonObject();
            int onlinePlayers = json.get("online-players").getAsInt();
            int maxPlayers = json.get("max-players").getAsInt();
            boolean whitelisted = json.get("whitelisted").getAsBoolean();
            return new Ping(onlinePlayers, maxPlayers, whitelisted, true);
        });
    }
}
