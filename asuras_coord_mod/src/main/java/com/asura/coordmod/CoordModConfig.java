package com.asura.coordmod;

import com.google.gson.JsonParser;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;

import java.nio.file.Files;
import java.nio.file.Path;

public class CoordModConfig {
    public static DataResult<Config> parseConfig(String json) {
        return Config.CODEC.parse(JsonOps.INSTANCE, JsonParser.parseString(json));
    }

    public static String encodeConfig(Config config) {
        return Config.CODEC.encodeStart(JsonOps.INSTANCE, config)
                .getOrThrow(error -> new RuntimeException("Failed to encode config: " + error))
                .toString();
    }

    public static Config loadConfig() {
        try {
            Path configPath = Path.of("config/asuras_coord_mod.json");
            if (Files.exists(configPath)) {
                String json = Files.readString(configPath);
                return parseConfig(json).getOrThrow(error -> new RuntimeException("Failed to parse config: " + error));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Config(); // Return default config if loading fails
    }

    public static void saveConfig(Config config) {
        try {
            Path configPath = Path.of("config/asuras_coord_mod.json");
            String json = encodeConfig(config);
            Files.createDirectories(configPath.getParent());
            Files.writeString(configPath, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}