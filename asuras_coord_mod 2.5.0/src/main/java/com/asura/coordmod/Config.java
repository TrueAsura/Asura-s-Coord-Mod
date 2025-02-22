package com.asura.coordmod;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class Config {
    public boolean showCoordinates;
    public boolean showDayCounter;
    public boolean showBlock; // New field

    // Updated constructor to include showBlock
    public Config(boolean showCoordinates, boolean showDayCounter, boolean showBlock) {
        this.showCoordinates = showCoordinates;
        this.showDayCounter = showDayCounter;
        this.showBlock = showBlock;
    }

    // Default constructor with default values
    public Config() {
        this(true, true, true); // Default values
    }

    // Updated CODEC to include showBlock
    public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.fieldOf("showCoordinates").forGetter(config -> config.showCoordinates),
            Codec.BOOL.fieldOf("showDayCounter").forGetter(config -> config.showDayCounter),
            Codec.BOOL.fieldOf("showBlock").forGetter(config -> config.showBlock) // New field
    ).apply(instance, Config::new));
}