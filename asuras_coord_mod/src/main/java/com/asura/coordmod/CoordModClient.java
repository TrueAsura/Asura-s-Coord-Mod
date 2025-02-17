package com.asura.coordmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;


@Environment(EnvType.CLIENT)
public class CoordModClient implements ClientModInitializer {
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
            String coords = String.format("§6X: §f%d §6Y: §f%d §6Z: §f%d", x, y, z);
            drawContext.drawText(client.textRenderer, Text.literal(coords), screenWidth / 2 - 45, screenHeight - 60, 0xFFFFFF, true);
        }

        if (config.showDayCounter) {
            int days = (int) (client.world.getTimeOfDay() / 24000L);
            String dayText = String.format("§lDay: %d", days);
            drawContext.drawText(client.textRenderer, Text.literal(dayText), 10, 10, 0xFFFFFF, true);
        }
    }
}
