package me.hyfe.queue.config;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class Config {
    private final String name;
    private final File file;
    private final Yaml yaml;
    private final Map<String, Object> map = new HashMap<>();
    private final Map<String, Set<String>> sections = new HashMap<>();

    public Config(String name, File file) throws IOException {
        this.name = name;
        this.file = file;
        this.yaml = new Yaml();
        this.createResource();
        this.load();
    }

    public Config(String name, String directory, UnaryOperator<Path> path) throws IOException, URISyntaxException {
        this.name = name;
        this.file = path.apply(this.createDirectory(directory).toAbsolutePath()).resolve(name).toFile();
        this.yaml = new Yaml();
        this.createResource();
        this.load();
    }

    public static Config create(String name, String directory, UnaryOperator<Path> path) {
        try {
            return new Config(name, directory, path);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Set<String> keys() {
        return this.map.keySet();
    }

    public String getName() {
        return this.name;
    }

    public boolean has(String key) {
        return this.map.containsKey(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T tryGet(String key) {
        return (T) this.get(key);
    }

    public Object get(String key) {
        return this.map.get(key);
    }

    public Set<String> getKeys(String path) {
        return this.sections.get(path);
    }

    public CompletableFuture<Void> reload() {
        return CompletableFuture.runAsync(() -> {
            try {
                this.load();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }).exceptionally((ex) -> {
            ex.printStackTrace();
            return null;
        });
    }

    private void load() throws IOException {
        FileInputStream inputStream = new FileInputStream(this.file);
        InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(streamReader);
        String content = this.concatContent(bufferedReader);
        Map<?, ?> input = this.yaml.load(content);
        this.writeContentToMemory(input);
    }

    private String concatContent(BufferedReader reader) throws IOException {
        try {
            return reader.lines().map((line) -> line.concat("\n")).collect(Collectors.joining());
        } finally {
            reader.close();
        }
    }

    private void writeContentToMemory(Map<?, ?> input) {
        this.map.clear();
        for (Map.Entry<?, ?> entry : input.entrySet()) {
            String key = entry.getKey().toString();
            Object value = entry.getValue();
            if (value instanceof Map) {
                Map<?, ?> section = ((Map<?, ?>) value);
                this.writeSectionToMemory(section, key);
                continue;
            }
            this.map.put(key, value);
        }
    }

    private void writeSectionToMemory(Map<?, ?> input, String parent) {
        for (Map.Entry<?, ?> entry : input.entrySet()) {
            String key = parent + "." + entry.getKey().toString();
            Object value = entry.getValue();
            if (!this.sections.containsKey(parent)) {
                this.sections.put(parent, new HashSet<>());
            }
            this.sections.get(parent).add(entry.getKey().toString());
            if (value instanceof Map) {
                Map<?, ?> section = ((Map<?, ?>) value);
                this.writeSectionToMemory(section, key);
                continue;
            }
            this.map.put(key, value);
        }
    }

    public Path createDirectory(String directory) throws URISyntaxException {
        String absolutePlatformPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        String platformPath = absolutePlatformPath.substring(0, absolutePlatformPath.lastIndexOf(File.separator));
        Path dataFolder = new File(platformPath).toPath().resolve(directory);
        if (!Files.exists(dataFolder)) {
            dataFolder.toFile().mkdirs();
        }
        return dataFolder;
    }

    private void createResource() {
        if (!Files.exists(this.file.toPath())) {
            this.saveResource();
        }
    }

    private void saveResource() {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(this.name);
        if (inputStream == null) {
            // TODO: log
        } else {
            try {
                byte[] bytes = new byte[inputStream.available()];
                inputStream.read(bytes);
                Files.write(this.file.toPath(), bytes, StandardOpenOption.CREATE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}