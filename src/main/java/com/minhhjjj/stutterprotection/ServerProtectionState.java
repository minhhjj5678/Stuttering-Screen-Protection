package com.minhhjjj.stutterprotection;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@EventBusSubscriber(modid = StutterProtection.MODID)
public class ServerProtectionState {

    private static final Set<UUID> protectedPlayers = new HashSet<>();
    private static final Map<UUID, Long> lastPingTime = new ConcurrentHashMap<>();
    private static MinecraftServer serverInstance = null;

    private static final long TIMEOUT_MS = 300;

    public static void onPingReceived(ServerPlayer player) {
        lastPingTime.put(player.getUUID(), System.currentTimeMillis());
    }

    public static boolean isProtected(ServerPlayer player) {
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
	public static void onServerStarted(ServerStartedEvent event) {
		serverInstance = event.getServer();
	}

    @SubscribeEvent
    public static void onPlayerLeft(PlayerEvent.PlayerLoggedOutEvent event) {
        UUID uuid = event.getEntity().getUUID();
        lastPingTime.remove(uuid);
        protectedPlayers.remove(uuid);
    }


    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        if (serverInstance == null) return;

        long now = System.currentTimeMillis();

        for (ServerPlayer player : serverInstance.getPlayerList().getPlayers()) {
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