package com.asura.coordmod;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class CoordModClient implements ClientModInitializer {
    // Use your custom textures
    private static final Identifier OVERWORLD_ICON = Identifier.of("asuras_coord_mod", "textures/block/grass_block_top.png");
    private static final Identifier NETHER_ICON = Identifier.of("asuras_coord_mod", "textures/block/netherrack.png");
    private static final Identifier END_ICON = Identifier.of("asuras_coord_mod", "textures/block/end_stone.png");

    private static Config config;

    @Override
    public void onInitializeClient() {
        // Load the config file
        config = CoordModConfig.loadConfig();

        // Register HUD rendering
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> onRender(drawContext));
    }

    private void onRender(DrawContext drawContext) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) return;

        int x = (int) client.player.getX();
        int y = (int) client.player.getY();
        int z = (int) client.player.getZ();

        int screenWidth = drawContext.getScaledWindowWidth();
        int screenHeight = drawContext.getScaledWindowHeight();

        if (config.showCoordinates) {
            String coords = String.format("X: %d Y: %d Z: %d", x, y, z);
            drawContext.drawText(client.textRenderer, Text.literal(coords), screenWidth / 2 - 30, screenHeight - 40, 0xFFFFFF, true);
        }

        if (config.showDimensionIcon) {
            Identifier icon = getDimensionIcon(client.world);
            // Use RenderLayer.getText for the texture layer
            drawContext.drawTexture((ignored) -> RenderLayer.getGuiOverlay(), icon, screenWidth / 2 + 50, screenHeight - 50, 0, 0, 16, 16, 16, 16);
        }

        if (config.showDayCounter) {
            int days = (int) (client.world.getTimeOfDay() / 24000L);
            String dayText = String.format("Minecraft Day: %d", days);
            drawContext.drawText(client.textRenderer, Text.literal(dayText), 10, 10, 0xFFFFFF, true);
        }
    }

    private Identifier getDimensionIcon(World world) {
        if (world.getRegistryKey() == World.OVERWORLD) {
            return OVERWORLD_ICON;
        } else if (world.getRegistryKey() == World.NETHER) {
            return NETHER_ICON;
        } else {
            return END_ICON;
        }
    }

    public static Screen createConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("title.asuras_coord_mod.config"));

        ConfigCategory category = builder.getOrCreateCategory(Text.translatable("category.asuras_coord_mod.general"));

        category.addEntry(builder.entryBuilder()
                .startBooleanToggle(Text.translatable("option.asuras_coord_mod.show_coordinates"), config.showCoordinates)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> config.showCoordinates = newValue)
                .build());

        category.addEntry(builder.entryBuilder()
                .startBooleanToggle(Text.translatable("option.asuras_coord_mod.show_dimension_icon"), config.showDimensionIcon)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> config.showDimensionIcon = newValue)
                .build());

        category.addEntry(builder.entryBuilder()
                .startBooleanToggle(Text.translatable("option.asuras_coord_mod.show_day_counter"), config.showDayCounter)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> config.showDayCounter = newValue)
                .build());

        // Save config when user closes the config screen
        builder.setSavingRunnable(() -> CoordModConfig.saveConfig(config));

        return builder.build();
    }
}