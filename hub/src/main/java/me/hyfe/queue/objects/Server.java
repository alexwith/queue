package me.hyfe.queue.objects;

import java.io.IOException;

public class Server {
    private final String ip;
    private final int port;
    private final String command;

    private Ping latestPing = new Ping(0, 0, false);

    public Server(String ip, int port, String command) {
        this.ip = ip;
        this.port = port;
        this.command = command;
    }

    public void ping() {
        try {
            this.latestPing = Ping.generate(this.ip, this.port);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Ping getLatestPing() {
        return this.latestPing;
    }
}
