package me.hyfe.queue.commands;

import me.hyfe.queue.BungeeQueueManager;
import me.hyfe.queue.QueuePlugin;
import me.hyfe.queue.config.keys.LangKeys;
import me.hyfe.queue.config.typekeys.LangKey;
import me.hyfe.queue.queue.Queue;
import me.hyfe.queue.text.Text;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class QueueAdminCommand extends Command {
    private final QueuePlugin plugin;

    public QueueAdminCommand(QueuePlugin plugin) {
        super("queueadmin", "queue.admin");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length != 2) {
            this.sendUsage(sender);
            return;
        }
        String action = args[0];
        String server = args[1];

        BungeeQueueManager queueManager = this.plugin.bootstrap().getQueueManager();
        Queue<?, ?> queue = queueManager.getQueue(server);
        if (queue == null) {
            this.sendLangKey(sender, LangKeys.QUEUE_NOT_FOUND);
            return;
        }
        switch (action.toLowerCase()) {
            case "pause": {
                queue.setPaused(true);
                this.sendLangKey(sender, LangKeys.QUEUE_PAUSED);
                break;
            }
            case "resume": {
                queue.setPaused(false);
                this.sendLangKey(sender, LangKeys.QUEUE_RESUMED);
                break;
            }
            case "clear": {
                queue.clear();
                this.sendLangKey(sender, LangKeys.QUEUE_CLEARED);
                break;
            }
            default: {
                this.sendUsage(sender);
            }
        }
    }

    private void sendUsage(CommandSender sender) {
        sender.sendMessage(new TextComponent("/queueadmin pause <queue>"));
        sender.sendMessage(new TextComponent("/queueadmin resume <queue>"));
        sender.sendMessage(new TextComponent("/queueadmin clear <queue>"));
    }

    private void sendLangKey(CommandSender sender, LangKey key) {
        sender.sendMessage(new TextComponent(Text.colorize(key.get())));
    }
}
