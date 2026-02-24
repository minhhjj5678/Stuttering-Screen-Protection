package com.minhhjjj.stutterprotection.network;

import net.minecraft.network.PacketBuffer;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

import com.minhhjjj.stutterprotection.ServerProtectionState;

public class LagStatusPacket {

    public LagStatusPacket() {}

    public static void encode(LagStatusPacket msg, PacketBuffer buf) {
    }

    public static LagStatusPacket decode(PacketBuffer buf) {
        return new LagStatusPacket();
    }

    public static void handle(LagStatusPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player != null) {
                ServerProtectionState.onPingReceived(player);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
