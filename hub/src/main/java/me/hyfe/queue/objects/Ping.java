package me.hyfe.queue.objects;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.hyfe.queue.redis.Redis;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class Ping {
    private final int onlinePlayers;
    private final int maxPlayers;
    private final boolean online;

    private static final JsonParser PARSER = new JsonParser();
    private static final BiFunction<String, Integer, URL> API_ROUTE = (ip, port) -> {
        try {
            return new URL(String.format("https://api.minetools.eu/ping/%s/%d", ip, port));
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
        return null;
    };

    public Ping(int onlinePlayers, int maxPlayers, boolean online) {
        this.onlinePlayers = onlinePlayers;
        this.maxPlayers = maxPlayers;
        this.online = online;
    }

    public int getOnlinePlayers() {
        return this.onlinePlayers;
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    public boolean isOnline() {
        return this.online;
    }

    public static Ping generate(Redis redis, String ip, int port) throws IOException {
        String response = null;
        if (redis != null) {
            response = getRedisResponse(redis, ip, port);
        }
        if (response == null) {
            response = getRestResponse(ip, port);
        }
        if (response == null) {
            return new Ping(0, 0, false);
        }
        JsonObject json = PARSER.parse(response).getAsJsonObject();
        if (json.has("error")) {
            return new Ping(0, 0, false);
        } else {
            int onlinePlayers = json.get("players").getAsJsonObject().get("online").getAsInt();
            int maxPlayers = json.get("players").getAsJsonObject().get("max").getAsInt();
            return new Ping(onlinePlayers, maxPlayers, true);
        }
    }

    private static String getRestResponse(String ip, int port) throws IOException {
        HttpsURLConnection connection = (HttpsURLConnection) API_ROUTE.apply(ip, port).openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");

        InputStreamReader inputReader = new InputStreamReader(connection.getInputStream());
        BufferedReader reader = new BufferedReader(inputReader);
        String response = reader.lines().collect(Collectors.joining());
        reader.close();
        return connection.getResponseCode() != 200 ? null : response;
    }

    private static String getRedisResponse(Redis redis, String ip, int port) {
        String id = ip + ":" + port;
        return redis.provideJedis((jedis) -> {
            return jedis.get(id);
        });
    }
}
