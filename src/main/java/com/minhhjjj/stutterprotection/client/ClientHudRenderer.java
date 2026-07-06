package com.minhhjjj.stutterprotection.client;

import com.minhhjjj.stutterprotection.config.LagConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.ChatScreen;
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

    @SuppressWarnings("null")
    public static void render(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();

        if (!LagConfig.SHOW_HUD.get()) return;
        if (mc.player == null || mc.level == null) return;
        if (mc.gui.hud.isHidden()) return;
        if (mc.gui.screen() instanceof ChatScreen) return;
        
        String mcText = "FPS: " + mcFps;
        int color = isLagging ? Color.GREEN.getRGB() : Color.WHITE.getRGB();
        int screenHeight = guiGraphics.guiHeight();
        int x = 5;
        int y = screenHeight - 10;

        /*// i use this for debug only
		String avgText = "Average FPS: " + averageFps;
        int avgX = 5;
        int avgY = screenHeight - 20;
		String sttText = "Stuttering Level: " + stutteringLevel;
        int sttX = 5;
        int sttY = screenHeight - 30;
		String dbText = "Time Left: " + debug;
        int dbX = 5;
        int dbY = screenHeight - 40;

        guiGraphics.drawString(mc.font, avgText, avgX, avgY, color);
        guiGraphics.drawString(mc.font, sttText, sttX, sttY, color);
        guiGraphics.drawString(mc.font, dbText, dbX, dbY, color);
		*/
        guiGraphics.text(mc.font, mcText, x, y, color);
    }
}