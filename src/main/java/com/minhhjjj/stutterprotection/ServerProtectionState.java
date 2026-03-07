package com.minhhjjj.stutterprotection;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

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

    public static void register() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            serverInstance = server;
            clearAll();
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            if (handler.player != null) {
                UUID uuid = handler.player.getUUID();
                lastPingTime.remove(uuid);
                protectedPlayers.remove(uuid);
            }
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            long now = System.currentTimeMillis();

            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                UUID uuid = player.getUUID();
                Long lastPing = lastPingTime.get(uuid);

                if (lastPing == null || (now - lastPing > TIMEOUT_MS)) {
                    protectedPlayers.add(uuid);
                } else {
                    protectedPlayers.remove(uuid);
                }
            }
        });
    }
}