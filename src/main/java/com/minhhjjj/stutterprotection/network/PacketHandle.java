package com.minhhjjj.stutterprotection.network;

import com.minhhjjj.stutterprotection.ServerProtectionState;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PacketHandle {
    public static void LagPacketHandleServerside(LagStatusPacket msg, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (player instanceof ServerPlayer serverPlayer) {
                ServerProtectionState.onPingReceived(serverPlayer);
            }
        });
    }

    public static void LagPacketHandleClientside(LagStatusPacket msg, IPayloadContext context) {} //pass
}