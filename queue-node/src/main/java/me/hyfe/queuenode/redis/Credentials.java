package me.hyfe.queuenode.redis;


import me.hyfe.queuenode.configs.RedisKeys;

public class Credentials {
    private final String address;
    private final int port;
    private final String password;

    public Credentials(String address, int port, String password) {
        this.address = address;
        this.port = port;
        this.password = password;
    }

    public static Credentials fromRedisKeys() {
        return new Credentials(RedisKeys.ADDRESS.get(), RedisKeys.PORT.get(), RedisKeys.PASSWORD.get());
    }

    public String getAddress() {
        return this.address;
    }

    public int getPort() {
        return this.port;
    }

    public String getPassword() {
        return this.password;
    }
}
