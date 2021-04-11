package me.hyfe.queuenode.configs;


import me.hyfe.helper.config.Config;
import me.hyfe.helper.config.KeysHolder;
import me.hyfe.helper.config.keys.LangKey;

public class LangKeys extends KeysHolder {

    public LangKeys() {
        super(Config.create("lang.yml", (path) -> path));
    }

    public static final LangKey JOINED_QUEUE = LangKey.ofKey(LangKeys.class, "joined-queue");
    public static final LangKey QUEUE_POSITION = LangKey.ofKey(LangKeys.class, "queue-position");
    public static final LangKey SENDING_SERVER = LangKey.ofKey(LangKeys.class, "sending-server");
    public static final LangKey SERVER_OFFLINE = LangKey.ofKey(LangKeys.class, "server-offline");
    public static final LangKey SERVER_WHITELISTED = LangKey.ofKey(LangKeys.class, "server-whitelisted");
    public static final LangKey ALREADY_QUEUED = LangKey.ofKey(LangKeys.class, "already-queued");
    public static final LangKey BYPASSING_QUEUE = LangKey.ofKey(LangKeys.class, "bypassing-queue");
    public static final LangKey ALREADY_ON_SERVER = LangKey.ofKey(LangKeys.class, "already-on-server");
    public static final LangKey JOIN_QUEUE_USAGE = LangKey.ofKey(LangKeys.class, "joinqueue-usage");
    public static final LangKey QUEUE_NOT_FOUND = LangKey.ofKey(LangKeys.class, "queue-not-found");
    public static final LangKey QUEUE_PAUSED = LangKey.ofKey(LangKeys.class, "queue-paused");
    public static final LangKey QUEUE_RESUMED = LangKey.ofKey(LangKeys.class, "queue-resumed");
    public static final LangKey QUEUE_CLEARED = LangKey.ofKey(LangKeys.class, "queue-cleared");
}
