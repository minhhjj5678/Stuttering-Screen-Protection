package com.minhhjjj.stutterprotection;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber(modid = StutterProtection.MODID)
public class ServerProtectionState {

    private static final Set<UUID> protectedPlayers = new HashSet<>();
    private static final Map<UUID, Long> lastPingTime = new ConcurrentHashMap<>();
    private static MinecraftServer serverInstance = null;

    private static final long TIMEOUT_MS = 300;

    public static void onPingReceived(ServerPlayerEntity player) {
        lastPingTime.put(player.getUUID(), System.currentTimeMillis());
    }

    public static boolean isProtected(ServerPlayerEntity player) {
        return protectedPlayers.contains(player.getUUID());
    }

    public static boolean isProtected(UUID uuid) {
        return protectedPlayers.contains(uuid);
    }

    public static void clearAll() {
        protectedPlayers.clear();
        lastPingTime.clear();
    }

    @SubscribeEvent
    public static void onServerStarted(FMLServerStartedEvent event) {
        serverInstance = event.getServer();
    }

    @SubscribeEvent
    public static void onPlayerLeft(PlayerEvent.PlayerLoggedOutEvent event) {
        PlayerEntity player = event.getPlayer();
        lastPingTime.remove(player.getUUID());
        protectedPlayers.remove(player.getUUID());
    }


    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || serverInstance == null) return;

        long now = System.currentTimeMillis();

        for (ServerPlayerEntity player : serverInstance.getPlayerList().getPlayers()) {
            UUID uuid = player.getUUID();
            Long lastPing = lastPingTime.get(uuid);

            if (lastPing == null || (now - lastPing > TIMEOUT_MS)) {
                protectedPlayers.add(uuid);
            } else {
                protectedPlayers.remove(uuid);
            }
        }
    }
}
