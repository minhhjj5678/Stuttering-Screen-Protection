package com.minhhjjj.stutterprotection.client;

import com.minhhjjj.stutterprotection.config.LagConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.network.chat.Component;

import java.awt.Color;

public class ClientHudRenderer {
    public static boolean isLagging = false;
    public static int currentFps = 0;
    public static int averageFps = 0;
    public static int stutteringLevel = 1;
    public static long debug;
    public static int mcFps;

    public static void updateLagStatus(boolean lag, int fps, int avg, int lvl, long debugTime, int minecraftFps) {
        isLagging = lag;
        currentFps = fps;
        averageFps = avg;
        stutteringLevel = lvl;
        debug = debugTime;
        mcFps = minecraftFps;
    }

    public static void render(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();

        if (!LagConfig.SHOW_HUD) return;
        if (mc.player == null || mc.level == null) return;
        if (mc.options.hideGui) return;
        if (mc.screen instanceof ChatScreen) return;
        
        String mcText = "FPS: " + mcFps;
        int color = isLagging ? Color.GREEN.getRGB() : Color.WHITE.getRGB();
        int x = 10;
        int y = guiGraphics.guiHeight() - 10;

        guiGraphics.text(mc.font, Component.literal(mcText), x, y, color, false);
    }
}