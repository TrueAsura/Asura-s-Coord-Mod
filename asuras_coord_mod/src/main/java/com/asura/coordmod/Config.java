package com.asura.coordmod;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class Config {
    public boolean showCoordinates;
    public boolean showDayCounter;

    public Config(boolean showCoordinates, boolean showDayCounter) {
        this.showCoordinates = showCoordinates;
        this.showDayCounter = showDayCounter;
    }

    public Config() {
        this(true, true); // Default values
    }

    public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.fieldOf("showCoordinates").forGetter(config -> config.showCoordinates),
            Codec.BOOL.fieldOf("showDayCounter").forGetter(config -> config.showDayCounter)
    ).apply(instance, Config::new));
}
