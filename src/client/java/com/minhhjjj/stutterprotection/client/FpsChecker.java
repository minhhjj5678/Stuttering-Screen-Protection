package com.minhhjjj.stutterprotection.client;

import com.minhhjjj.stutterprotection.network.LagStatusPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;

import java.util.ArrayDeque;
import java.util.Queue;

public class FpsChecker {
    private static boolean isProtecting = false;
    private static long gracePeriodEnd = 0;
    private static long debug = 0;

    private static int graceTimeMs;
    private static int stutteringLevel = 1;
    private static int additionalLevel = 0;
    private static final int FPS_THRESHOLD = 18;
    private static final int MULTIPLIER = 8; // 1 sec = 8 level

    private static final Queue<Integer> fpsHistory = new ArrayDeque<>();
    private static final int HISTORY_SIZE = 40;
    private static int averageFps = 0;
    
    private static long lastRenderTime = 0;
    private static long lastLogicTime = 0;

    public static void onRenderFrame() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;
        
        long nanoNow = System.nanoTime();
        if(lastRenderTime == 0) {
            lastRenderTime = nanoNow;
            return;
        }
        
        long nanoTimeFrame = nanoNow - lastRenderTime;
        lastRenderTime = nanoNow;
        if (nanoTimeFrame == 0) return;
        int instantFps = (int) (1_000_000_000L/nanoTimeFrame);
        long timeFrameMs = nanoTimeFrame/1_000_000;
        
        long now = System.currentTimeMillis();
        if (now - lastLogicTime < 100) return;
        lastLogicTime = now;

        int currentFps = instantFps;
        int maxFpsSetting = mc.options.framerateLimit().get();
        if (maxFpsSetting < 260 && currentFps >= (maxFpsSetting-3)) {
            fpsHistory.clear();
            for(int i = 0; i < HISTORY_SIZE; i++) fpsHistory.add(currentFps);
            averageFps = currentFps;
        } else updateFpsHistory(currentFps);

        boolean isLaggingNow1, isLaggingNow2;
        boolean isLowFps = currentFps <= FPS_THRESHOLD;

        if (fpsHistory.size() < 20) {
            isLaggingNow1 = isLowFps;
        } else {
            boolean isSuddenDrop = currentFps <= (averageFps * 0.8);
            isLaggingNow1 = isLowFps && isSuddenDrop;
        }
        
        isLaggingNow2 = maxFpsSetting > 20 && currentFps <= 10;

        now = System.currentTimeMillis();

        if (isLaggingNow1 || isLaggingNow2) {
            if (!isProtecting) isProtecting = true;

            if (stutteringLevel < 80) stutteringLevel++;
            additionalLevel = (int) (timeFrameMs*MULTIPLIER/1000);
            stutteringLevel += (additionalLevel>20) ? 20 : additionalLevel;
            gracePeriodEnd = 0;
        } else if (stutteringLevel > 0) stutteringLevel--;

        if (!isLaggingNow1 && !isLaggingNow2 && isProtecting) {
            if (gracePeriodEnd == 0) {
                int graceTimeMsTmp = 0;
                if (stutteringLevel < 1) graceTimeMs = 0;
                else if (stutteringLevel < 31) graceTimeMsTmp = 250;
                else if (stutteringLevel < 41) graceTimeMsTmp = 1000;
                else if (stutteringLevel < 61) graceTimeMsTmp = 2000;
                else if (stutteringLevel < 81) graceTimeMsTmp = 3000;
                else graceTimeMsTmp = 3500;
                if (graceTimeMsTmp > graceTimeMs) {
                    graceTimeMs = graceTimeMsTmp;
                    gracePeriodEnd = now + graceTimeMsTmp;
                } else gracePeriodEnd = now + graceTimeMs;
            }

            if (now >= gracePeriodEnd) {
                isProtecting = false;
                sendPacket(mc);
                gracePeriodEnd = 0;
                graceTimeMs = 0;
            }
        }

        if (!isLaggingNow1 && !isLaggingNow2 && !isProtecting) {
            sendPacket(mc);
        }

        debug = gracePeriodEnd - now;
        ClientHudRenderer.updateLagStatus(isProtecting, currentFps, averageFps, stutteringLevel, debug, mc.getFps());
    }

    private static void updateFpsHistory(int currentFps) {
        fpsHistory.add(currentFps);
        if (fpsHistory.size() > HISTORY_SIZE) {
            fpsHistory.remove();
        }

        if (fpsHistory.isEmpty()) return;
        long sum = 0;
        for (int f : fpsHistory) sum += f;
        averageFps = (int) (sum / fpsHistory.size());
    }

    private static void sendPacket(Minecraft mc) {
        if (mc.getConnection() != null) {
            ClientPlayNetworking.send(LagStatusPacket.PACKET);
        }
    }
}