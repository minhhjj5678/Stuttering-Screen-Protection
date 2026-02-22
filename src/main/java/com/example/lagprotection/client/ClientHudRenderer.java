package com.example.lagprotection.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.example.lagprotection.config.LagConfig;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;

import java.awt.*;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ClientHudRenderer {
    private static boolean isLagging = false;
    private static int currentFps = 0;
    private static int mcFps = 0;
    private static int avgFps = 0;
    private static int lvl = 1;
    private static long debug;

    public static void updateLagStatus(boolean isLagging, int currentFps, int mcFps, int avgFps, int lvl, long debug) {
        ClientHudRenderer.isLagging = isLagging;
        ClientHudRenderer.currentFps = currentFps;
        ClientHudRenderer.mcFps = mcFps;
        ClientHudRenderer.avgFps = avgFps;
        ClientHudRenderer.lvl = lvl;
        ClientHudRenderer.debug = debug;
    }

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;
        if (mc.screen instanceof ChatScreen) return;
        if (mc.options.hideGui) return;
        if (!LagConfig.SHOW_HUD.get()) return;
        if (event.getOverlay() != VanillaGuiOverlay.HOTBAR.type()) return;

        GuiGraphics guiGraphics = event.getGuiGraphics();
        String text = "FPS: " + mcFps;
        int color = isLagging ? Color.GREEN.getRGB() : Color.WHITE.getRGB();

        int screenHeight = mc.getWindow().getGuiScaledHeight();
        int x = 5;
        int y = screenHeight - 10;

        /*// i use this for debug only
        String avgText = "Average FPS: " + avgFps;
        int avgX = 5;
        int avgY = screenHeight - 20;
        String sttText = "Stuttering Level: " + lvl;
        int sttX = 5;
        int sttY = screenHeight - 30;
        
        String dbText = "Time Left: " + debug;
        int dbX = 5;
        int dbY = screenHeight - 40;
        
        guiGraphics.drawString(mc.font, avgText, avgX, avgY, color);
        guiGraphics.drawString(mc.font, sttText, sttX, sttY, color);
        guiGraphics.drawString(mc.font, dbText, dbX, dbY, color);
        */
        guiGraphics.drawString(mc.font, text, x, y, color);
    }
}
