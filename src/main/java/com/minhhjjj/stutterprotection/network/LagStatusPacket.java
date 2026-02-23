package com.minhhjjj.stutterprotection.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import com.minhhjjj.stutterprotection.ServerProtectionState;

public class LagStatusPacket {

    public LagStatusPacket() {}

    public static void encode(LagStatusPacket msg, FriendlyByteBuf buf) {
    }

    public static LagStatusPacket decode(FriendlyByteBuf buf) {
        return new LagStatusPacket();
    }

    public static void handle(LagStatusPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                ServerProtectionState.onPingReceived(player);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
