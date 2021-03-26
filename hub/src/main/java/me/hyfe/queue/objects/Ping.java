package me.hyfe.queue.objects;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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

    // Thank you ServerSelectorX (modified), taken under license GNU General Public License v3.0
    public static Ping generate(String ip, int port) throws IOException {
        HttpsURLConnection connection = (HttpsURLConnection) API_ROUTE.apply(ip, port).openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");

        InputStreamReader inputReader = new InputStreamReader(connection.getInputStream());
        BufferedReader reader = new BufferedReader(inputReader);
        String response = reader.lines().collect(Collectors.joining());
        reader.close();

        if (connection.getResponseCode() != 200) {
            throw new RuntimeException("Failed to ping the server " + ip + ":" + port + ". Status code: " + connection.getResponseCode());
        }
        JsonParser jsonParser = new JsonParser();
        JsonObject json = jsonParser.parse(response).getAsJsonObject();
        if (json.has("error")) {
            return new Ping(0, 0, false);
        } else {
            int onlinePlayers = json.get("players").getAsJsonObject().get("online").getAsInt();
            int maxPlayers = json.get("players").getAsJsonObject().get("max").getAsInt();
            return new Ping(onlinePlayers, maxPlayers, true);
        }
    }
}
