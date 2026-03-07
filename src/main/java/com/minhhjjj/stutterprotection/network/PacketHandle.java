package com.minhhjjj.stutterprotection.network;

import com.minhhjjj.stutterprotection.ServerProtectionState;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;

public class PacketHandle {
    public static void LagPacketHandleServerside(LagStatusPacket msg, ServerPlayNetworking.Context context) {
        context.server().execute(() -> {
            ServerPlayer serverPlayer = context.player();
            ServerProtectionState.onPingReceived(serverPlayer);
        });
    }
}