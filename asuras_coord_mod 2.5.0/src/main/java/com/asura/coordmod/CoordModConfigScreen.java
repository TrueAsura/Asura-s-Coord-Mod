package com.asura.coordmod;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class CoordModConfigScreen extends Screen {

    private final CoordModConfig config;
    private final Screen parent;

    public CoordModConfigScreen(Screen parent) {
        super(Text.of("Coord Mod Settings"));
        this.parent = parent; // Store the parent screen
        this.config = CoordModConfig.loadConfig(); // Load the config
    }

    @Override
    protected void init() {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent) // Set the parent screen
                .setTitle(Text.of("Coord Mod Settings"))
                .setSavingRunnable(() -> {
                    // Save the config when "Done" is clicked
                    CoordModConfig.saveConfig(config);
                });

        ConfigCategory general = builder.getOrCreateCategory(Text.of("General")); // Create a category
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        // Add a boolean toggle for "Show Coordinates"
        general.addEntry(entryBuilder.startBooleanToggle(Text.of("Show Coordinates"), config.config.showCoordinates)
                .setDefaultValue(true) // Optional: Set a default value
                .setSaveConsumer(newValue -> config.config.showCoordinates = newValue) // Save the new value
                .build());

        // Add a boolean toggle for "Show Day Counter"
        general.addEntry(entryBuilder.startBooleanToggle(Text.of("Show Day Counter"), config.config.showDayCounter)
                .setDefaultValue(true) // Optional: Set a default value
                .setSaveConsumer(newValue -> config.config.showDayCounter = newValue) // Save the new value
                .build());

        // Add a boolean toggle for "Show Block Next to Coordinates"
        general.addEntry(entryBuilder.startBooleanToggle(Text.of("Show Dimension"), config.config.showBlock)
                .setDefaultValue(true) // Optional: Set a default value
                .setSaveConsumer(newValue -> config.config.showBlock = newValue) // Save the new value
                .build());

        // Build and set the screen
        if (this.client != null) {
            this.client.setScreen(builder.build());
        }
    }
}