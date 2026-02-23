package com.minhhjjj.stutterprotection.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent.RenderTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Queue;

import com.minhhjjj.stutterprotection.network.LagStatusPacket;
import com.minhhjjj.stutterprotection.network.PacketHandle;

import java.util.ArrayDeque;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class FpsChecker {
    private static final int FPS_THRESGOLD = 18;
    private static boolean isProtecting = false;
    private static long gracePeriodEnd = 0;
    
    private static final Queue<Integer> fpsHistory = new ArrayDeque<>();
    private static final int HISTORY_MAX_SIZE = 40;
    private static int fpsAverage;

    private static long lastRenderTime = 0;
    private static long lastLogicTime = 0;

    private static final int SCALE = 4;

    private static final int MULTIPLIER = 8; // 1 second ~ 8 level
    private static int stutteringLevel = 1;
    private static int graceTime = 0;

    public static boolean isCurrentlyProtected() {
        return isProtecting;
    }


    @SubscribeEvent
    public static void onRenderTick(RenderTickEvent event) {
        long now = System.currentTimeMillis();
        long nanoNow = System.nanoTime();
        if (event.phase != RenderTickEvent.Phase.END) return;
        if (lastRenderTime == 0) {
            lastRenderTime = nanoNow;
            return;
        }
        long frameTime = nanoNow - lastRenderTime;
        lastRenderTime = nanoNow;

        if (now - lastLogicTime < 100) return;
        lastLogicTime = now;
        if (frameTime == 0) return;
        int instantFps = (int) (1_000_000_000L / frameTime);
        long frameTimeMs = frameTime / 1_000_000;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;

        int mcFps = mc.getFps();
        int fps = instantFps;
        int maxFps = mc.options.framerateLimit().get();

        boolean isLowFps = fps <= FPS_THRESGOLD;
        boolean isInstantDrop, isLagging1;
        if (maxFps < 260 && fps >= (maxFps - SCALE)) {
            fpsHistory.clear();
            for (int i = 0; i<HISTORY_MAX_SIZE; i++) fpsHistory.add(fps);
            fpsAverage = fps;
        } else updateFpsHistory(fps);
        
        if (fpsHistory.size() < 20) {
            isLagging1 = isLowFps;
        } else {
            isInstantDrop = fps <= (fpsAverage * 0.8);
            isLagging1 = isLowFps && isInstantDrop;
        }

        boolean isLagging2 = (fps<10 && maxFps>20);
        boolean isLaggingNow = isLagging1 || isLagging2;
        int additionalLevel = 0;

        int graceTimeTmp = 0;
        if (isLaggingNow) {
            if (!isProtecting) {
                isProtecting = true;
            }
            if (stutteringLevel < 80) stutteringLevel += 1;
            additionalLevel = (int) (frameTimeMs * MULTIPLIER / 1000);
            additionalLevel = (additionalLevel > 100) ? 100 : (additionalLevel < 0) ? 0 : additionalLevel;
            gracePeriodEnd = 0; // reset
        } else stutteringLevel -= 1;

        stutteringLevel = (stutteringLevel < 0) ? 0 : stutteringLevel;
        stutteringLevel += additionalLevel;
        stutteringLevel = (stutteringLevel > 100) ? 100 : (stutteringLevel < 0) ? 0 : stutteringLevel;

        if (!isLaggingNow && isProtecting) {
            if (gracePeriodEnd == 0) {
                if (stutteringLevel < 1) graceTimeTmp = 0;
                else if (stutteringLevel < 31) graceTimeTmp = 250;
                else if (stutteringLevel < 41) graceTimeTmp = 1000;
                else if (stutteringLevel < 61) graceTimeTmp = 2000;
                else if (stutteringLevel < 81) graceTimeTmp = 3000;
                else graceTimeTmp = 3500;

                if (graceTimeTmp > graceTime) {
                    graceTime = graceTimeTmp;
                    gracePeriodEnd = now + graceTimeTmp;
                } else gracePeriodEnd = now + graceTime;
            }

            if (now >= gracePeriodEnd) {
                isProtecting = false;
                if (mc.getConnection() != null) PacketHandle.INSTANCE.sendToServer(new LagStatusPacket());
                gracePeriodEnd = 0; // reset
                graceTime = 0;
            }
        }

        if (!isLaggingNow && !isProtecting) {
            if (mc.getConnection() != null) PacketHandle.INSTANCE.sendToServer(new LagStatusPacket());
        }
        long debug = (gracePeriodEnd - now > 0) ? (gracePeriodEnd - now) : 0;
        ClientHudRenderer.updateLagStatus(isProtecting, fps, mcFps, fpsAverage, stutteringLevel, debug);
    }

    private static void updateFpsHistory(int fps) {
        fpsHistory.add(fps);
        if(fpsHistory.size() > HISTORY_MAX_SIZE) fpsHistory.remove();
        if(fpsHistory.isEmpty()) return;
        int size = fpsHistory.size();
        long sum = 0;
        for(int f : fpsHistory) sum += f;
        fpsAverage = (int) (sum/size);
    }
}
