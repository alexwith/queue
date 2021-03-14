package me.hyfe.queue.config.keys;

import me.hyfe.queue.config.Config;
import me.hyfe.queue.config.KeysHolder;
import me.hyfe.queue.config.typekeys.LangKey;

public class LangKeys extends KeysHolder {

    public LangKeys() {
        super(Config.create("lang.yml", "queue", (path) -> path));
    }

    public static final LangKey JOINED_QUEUE = LangKey.ofKey(LangKeys.class, "joined-queue");
    public static final LangKey QUEUE_POSITION = LangKey.ofKey(LangKeys.class, "queue-position");
    public static final LangKey SENDING_SERVER = LangKey.ofKey(LangKeys.class, "sending-server");
    public static final LangKey SERVER_OFFLINE = LangKey.ofKey(LangKeys.class, "server-offline");
    public static final LangKey SERVER_WHITELISTED = LangKey.ofKey(LangKeys.class, "server-whitelisted");
    public static final LangKey ALREADY_QUEUED = LangKey.ofKey(LangKeys.class, "already-queued");
    public static final LangKey BYPASSING_QUEUE = LangKey.ofKey(LangKeys.class, "bypassing-queue");
    public static final LangKey ALREADY_ON_SERVER = LangKey.ofKey(LangKeys.class, "already-on-server");
}
