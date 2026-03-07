package com.minhhjjj.stutterprotection.client;

import com.minhhjjj.stutterprotection.config.ClientCommands;
import com.minhhjjj.stutterprotection.config.LagConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.network.chat.Component;

import java.awt.Color;
import java.lang.reflect.Method;

public class ClientHudRenderer implements ClientModInitializer {
    public static boolean isLagging = false;
    public static int currentFps = 0;
    public static int averageFps = 0;
    public static int stutteringLevel = 1;
    public static long debug;
    public static int mcFps;

    // --- BIẾN CACHE CHO REFLECTION (Để không bị lag FPS) ---
    private static Method drawMethod = null;
    private static boolean isMethodResolved = false;
    private static boolean usesComponent = false;
    private static boolean usesShadow = false;

    @Override
    public void onInitializeClient() {
        LagConfig.load();
        HudRenderCallback.EVENT.register((guiGraphics, deltaTracker) -> {
            FpsChecker.onRenderFrame();
            ClientHudRenderer.render(guiGraphics, deltaTracker);
        });
        ClientCommands.register();
    }

    public static void updateLagStatus(boolean lag, int fps, int avg, int lvl, long debugTime, int minecraftFps) {
        isLagging = lag;
        currentFps = fps;
        averageFps = avg;
        stutteringLevel = lvl;
        debug = debugTime;
        mcFps = minecraftFps;
    }

    public static void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();

        if (!LagConfig.SHOW_HUD) return;
        if (mc.player == null || mc.level == null) return;
        if (mc.options.hideGui) return;
        if (mc.screen instanceof ChatScreen) return;
        
        String mcText = "FPS: " + mcFps;
        int color = isLagging ? Color.GREEN.getRGB() : Color.WHITE.getRGB();
        int x = 10;
        int y = guiGraphics.guiHeight() - 10;

        drawSafe(guiGraphics, mc.font, mcText, x, y, color);
    }

    private static void drawSafe(GuiGraphics guiGraphics, Font font, String text, int x, int y, int color) {
        if (!isMethodResolved) {
            resolveDrawMethod();
        }

        if (drawMethod == null) return; 

        try {
            Object textArg = usesComponent ? Component.literal(text) : text;
            
            if (usesShadow) {
                drawMethod.invoke(guiGraphics, font, textArg, x, y, color, false);
            } else {
                drawMethod.invoke(guiGraphics, font, textArg, x, y, color);
            }
        } catch (Exception ignored) {
        }
    }

    private static void resolveDrawMethod() {
        for (Method m : GuiGraphics.class.getMethods()) {
            Class<?>[] params = m.getParameterTypes();
            
            if (params.length == 5 || params.length == 6) {
                if (params[0] == Font.class && params[2] == int.class && params[3] == int.class && params[4] == int.class) {
                    
                    if (params[1] == String.class) {
                        usesComponent = false;
                    } else if (params[1] == Component.class) {
                        usesComponent = true;
                    } else {
                        continue;
                    }

                    if (params.length == 6 && params[5] == boolean.class) {
                        usesShadow = true;
                    } else if (params.length == 5) {
                        usesShadow = false;
                    } else {
                        continue;
                    }

                    drawMethod = m;
                    break;
                }
            }
        }
        isMethodResolved = true;
    }
}