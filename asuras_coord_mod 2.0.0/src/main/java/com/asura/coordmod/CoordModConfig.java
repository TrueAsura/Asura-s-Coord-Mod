package com.asura.coordmod;

import com.google.gson.JsonParser;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import java.nio.file.Files;
import java.nio.file.Path;

public class CoordModConfig {
    private static final Path CONFIG_PATH = Path.of("config/asuras_coord_mod.json");

    // Use Config class for the actual configuration
    public Config config;

    public CoordModConfig(Config config) {
        this.config = config;
    }

    public CoordModConfig() {
        this(new Config()); // Default values
    }

    public static DataResult<CoordModConfig> parseConfig(String json) {
        return Config.CODEC.parse(JsonOps.INSTANCE, JsonParser.parseString(json)).map(config -> new CoordModConfig(config));
    }

    public static String encodeConfig(CoordModConfig config) {
        return Config.CODEC.encodeStart(JsonOps.INSTANCE, config.config)
                .getOrThrow(error -> new RuntimeException("Failed to encode config: " + error))
                .toString();
    }

    public static CoordModConfig loadConfig() {
        try {
            if (Files.exists(CONFIG_PATH)) {
                String json = Files.readString(CONFIG_PATH);
                return parseConfig(json).getOrThrow(error -> new RuntimeException("Failed to parse config: " + error));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new CoordModConfig(); // Default values
    }

    public static void saveConfig(CoordModConfig config) {
        try {
            String json = encodeConfig(config);
            Files.createDirectories(CONFIG_PATH.getParent());
            Files.writeString(CONFIG_PATH, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
