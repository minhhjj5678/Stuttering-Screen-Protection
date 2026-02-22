package com.example.lagprotection.client;

import com.example.lagprotection.config.LagConfig;
import com.example.lagprotection.network.LagStatusPacket;
import com.example.lagprotection.network.PacketHandle;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.Timer;
import java.util.TimerTask;

public class FpsChecker {
    private static boolean currentlyLagging = false;
    private static long gracePeriodEnd = 0; // thời điểm kết thúc thời gian chờ

    public static boolean isCurrentlyProtected() {
        return currentlyLagging;
    }

    public static int getCurrentFps() {
        try {
            String fpsStr = Minecraft.getInstance().fpsString;
            return Integer.parseInt(fpsStr.split(" ")[0]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            return -1;
        }
    }

    public static void start() {
        if (!FMLEnvironment.dist.isClient()) return;

        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(ClientHudRenderer.class);

        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Minecraft mc = Minecraft.getInstance();
                if (mc.player == null || mc.level == null) return;

                int currentFps = getCurrentFps();
                int threshold = LagConfig.FPS_THRESHOLD.get();
                int graceTime = LagConfig.PROTECTION_DURATION_MS.get(); // thời gian chờ (ms)

                long now = System.currentTimeMillis();
                boolean isLaggingNow = currentFps != -1 && currentFps <= threshold;

                if (isLaggingNow) {
                    if (!currentlyLagging) {
                        currentlyLagging = true;
                    }
                    gracePeriodEnd = 0; // reset
                }

                if (!isLaggingNow && currentlyLagging) {
                    if (gracePeriodEnd == 0) {
                        gracePeriodEnd = now + graceTime;
                    }

                    if (now >= gracePeriodEnd) {
                        currentlyLagging = false;
                        PacketHandle.INSTANCE.sendToServer(new LagStatusPacket());
                        gracePeriodEnd = 0; // reset
                    }
                }

                if (!isLaggingNow && !currentlyLagging) {
                    PacketHandle.INSTANCE.sendToServer(new LagStatusPacket());
                }

                ClientHudRenderer.updateLagStatus(currentlyLagging, currentFps);
            }
        }, 0, 100);
    }
}
