package com.example.lagprotection.client;

import com.example.lagprotection.config.LagConfig;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ClientHudRenderer {
    private static boolean isLagging = false;
    private static int currentFps = 0;

    public static void updateLagStatus(boolean lag, int fps) {
        isLagging = lag;
        currentFps = fps;
    }

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null || mc.level == null) return;
        if (!LagConfig.SHOW_HUD.get()) return;
        if (mc.screen instanceof net.minecraft.client.gui.screens.ChatScreen) return;

        String text = "FPS: " + currentFps;
        int color = isLagging ? Color.GREEN.getRGB() : Color.WHITE.getRGB();

        int x = 5;
        int y = mc.getWindow().getGuiScaledHeight() - 10;

        mc.font.draw(event.getPoseStack(), text, x, y, color);
    }
}
