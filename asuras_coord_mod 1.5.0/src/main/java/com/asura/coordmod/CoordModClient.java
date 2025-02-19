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
        if (client.player == null || client.world == null || client.options.hudHidden) return;

        int x = (int) client.player.getX();
        int y = (int) client.player.getY();
        int z = (int) client.player.getZ();

        int screenWidth = drawContext.getScaledWindowWidth();
        int screenHeight = drawContext.getScaledWindowHeight();

        if (config.showCoordinates) {
            // Use Minecraft formatting codes for bold gold labels and normal white numbers
            String coordsString = "§6§lX: §f" + x + " §6§lY: §f" + y + " §6§lZ: §f" + z;
            Text coords = Text.of(coordsString);

            // Draw the coordinates (position remains unchanged)
            drawContext.drawText(client.textRenderer, coords, screenWidth / 2 - 45, screenHeight - 60, 0xFFFFFF, true);
        }

        if (config.showDayCounter) {
            int days = (int) (client.world.getTimeOfDay() / 24000L);
            // Use Minecraft formatting codes for bold white label and normal white number
            String dayString = "§f§lDay: §f" + days;
            Text dayText = Text.of(dayString);

            // Draw the day counter (position remains unchanged)
            drawContext.drawText(client.textRenderer, dayText, 10, 10, 0xFFFFFF, true);
        }
    }
}