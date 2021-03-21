package me.hyfe.queue.bootstrap;

import me.hyfe.queue.config.ConfigController;
import me.hyfe.queue.config.keys.ConfigKeys;
import me.hyfe.queue.config.keys.LangKeys;
import me.hyfe.queue.config.keys.RedisKeys;
import me.hyfe.queue.queue.QueueManager;
import me.hyfe.queue.redis.Credentials;
import me.hyfe.queue.redis.Redis;
import me.hyfe.queue.redis.RedisProvider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Bootstrap implements RedisProvider {
    private final ConfigController configController;
    private final Redis redis;
    private final QueueManager<?, ?> queueManager;

    private static Bootstrap instance;

    public static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(10);
    public static final ScheduledExecutorService SCHEDULED_EXECUTOR = Executors.newScheduledThreadPool(10);

    public Bootstrap(BootstrapProvider<?, ?> parent) {
        instance = this;
        this.configController = new ConfigController();
        this.configController.registerConfigs(
                new RedisKeys(),
                new ConfigKeys(),
                new LangKeys()
        );
        this.redis = Redis.createInstance(Credentials.fromRedisKeys());
        this.queueManager = parent.createQueueManager();
        parent.registerListeners();
        //this.test();
    }

    public static Bootstrap create(BootstrapProvider<?, ?> parent) {
        return new Bootstrap(parent);
    }

    public static Bootstrap get() {
        return instance;
    }

    public ConfigController getConfigController() {
        return this.configController;
    }

    @Override
    public @NotNull Redis getRedis() {
        return this.redis;
    }

    @SuppressWarnings("unchecked")
    public <T extends QueueManager<?, ?>> T getQueueManager() {
        return (T) this.queueManager;
    }

    public void terminate() {
        this.queueManager.clearQueues();
        this.redis.close();
    }

    /**
     * Bug testing code for future bugs
     */
    /*private void test() {
        RedisQueue<TestObject> queue = new RedisQueue<TestObject>(this, "test-queue") {

            @Override
            public String encode(TestObject value) {
                return value.name + ":" + value.priority;
            }

            @Override
            public TestObject decode(String string) {
                String[] args = string.split(":");
                return new TestObject(args[0], Integer.parseInt(args[1]));
            }
        };
        queue.queue(new TestObject("player1", 0));
        queue.queue(new TestObject("player2", 10));
        queue.queue(new TestObject("player3", 0));
        queue.queue(new TestObject("player4", 0));
        queue.queue(new TestObject("player5", 500));
        queue.queue(new TestObject("player6", 0));
        queue.queue(new TestObject("player7", 0));
        queue.queue(new TestObject("player8", 0));
        queue.queue(new TestObject("player9", 1));
        queue.queue(new TestObject("player10", 501));

        while (queue.length() > 0) {
            TestObject object = queue.poll();
            System.out.println(object.name + " - Priority: " + object.priority);
        }
    }

    class TestObject implements Comparable<TestObject> {
        private final String name;
        private final int priority;

        public TestObject(String name, int priority) {
            this.name = name;
            this.priority = priority;
        }

        @Override
        public int compareTo(@NotNull TestObject o) {
            if (this.priority == o.priority) {
                return 1;
            }
            //System.out.println(this.priority + ":" + o.priority + "-" + Integer.compare(this.priority, o.priority));
            return Integer.compare(this.priority, o.priority);
        }
    }*/
}
