package com.asura.coordmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class CoordModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Register HUD rendering
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> onRender(drawContext));
    }

    private void onRender(DrawContext drawContext) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null || client.options.hudHidden) return;

        // Load the latest config
        CoordModConfig config = CoordModConfig.loadConfig();

        int x = (int) client.player.getX();
        int y = (int) client.player.getY();
        int z = (int) client.player.getZ();

        int screenWidth = drawContext.getScaledWindowWidth();
        int screenHeight = drawContext.getScaledWindowHeight();

        if (config.config.showCoordinates) { // Access through config.config
            String coordsString = String.format("§6§lX: §f%d §6§lY: §f%d §6§lZ: §f%d", x, y, z);
            Text coords = Text.of(coordsString);

            // Draw the coordinates text
            drawContext.drawText(client.textRenderer, coords, screenWidth / 2 - 50, screenHeight - 60, 0xFFFFFF, true);

            // Draw the block next to the coordinates if enabled in the config
            if (config.config.showBlock) {
                ItemStack blockToDisplay;
                if (client.world.getRegistryKey() == World.NETHER) {
                    blockToDisplay = Items.NETHERRACK.getDefaultStack(); // Netherrack in the Nether
                } else if (client.world.getRegistryKey() == World.END) {
                    blockToDisplay = Items.END_STONE.getDefaultStack(); // End stone in the End
                } else {
                    blockToDisplay = Items.GRASS_BLOCK.getDefaultStack(); // Grass block in the Overworld
                }

                // Draw the block next to the coordinates
                drawContext.drawItem(blockToDisplay, screenWidth / 2 - 8, screenHeight - 52);
            }
        }

        if (config.config.showDayCounter) { // Access through config.config
            int days = (int) (client.world.getTimeOfDay() / 24000L);
            String dayString = String.format("§f§lDay: §f%d", days);
            Text dayText = Text.of(dayString);

            drawContext.drawText(client.textRenderer, dayText, 10, 10, 0xFFFFFF, true);
        }
    }
}