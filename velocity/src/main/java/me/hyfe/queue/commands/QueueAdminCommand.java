package me.hyfe.queue.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import me.hyfe.queue.QueuePlugin;
import me.hyfe.queue.VelocityQueueManager;
import me.hyfe.queue.config.keys.LangKeys;
import me.hyfe.queue.config.typekeys.LangKey;
import me.hyfe.queue.queue.Queue;
import me.hyfe.queue.text.Text;
import net.kyori.adventure.text.Component;

public class QueueAdminCommand implements SimpleCommand {
    private final QueuePlugin plugin;

    public QueueAdminCommand(QueuePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (args.length != 2) {
            this.sendUsage(source);
            return;
        }
        String action = args[0];
        String server = args[1];

        VelocityQueueManager queueManager = this.plugin.bootstrap().getQueueManager();
        Queue<?, ?> queue = queueManager.getQueue(server);
        if (queue == null) {
            this.sendLangKey(source, LangKeys.QUEUE_NOT_FOUND);
            return;
        }
        switch (action.toLowerCase()) {
            case "pause": {
                queue.setPaused(true);
                this.sendLangKey(source, LangKeys.QUEUE_PAUSED);
                break;
            }
            case "resume": {
                queue.setPaused(false);
                this.sendLangKey(source, LangKeys.QUEUE_RESUMED);
                break;
            }
            case "clear": {
                queue.clear();
                this.sendLangKey(source, LangKeys.QUEUE_CLEARED);
                break;
            }
            default: {
                this.sendUsage(source);
            }
        }
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("queue.admin");
    }

    private void sendUsage(CommandSource source) {
        source.sendMessage(Component.text("/queueadmin pause <queue>"));
        source.sendMessage(Component.text("/queueadmin resume <queue>"));
        source.sendMessage(Component.text("/queueadmin clear <queue>"));
    }

    private void sendLangKey(CommandSource source, LangKey key) {
        source.sendMessage(Component.text(Text.colorize(key.get())));
    }
}
