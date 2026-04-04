package com.minhhjjj.stutterprotection.network;

import com.minhhjjj.stutterprotection.StutterProtection;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public class LagStatusPacket implements CustomPacketPayload {
    
    public static final CustomPacketPayload.Type<LagStatusPacket> TYPE = 
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(StutterProtection.MODID, "payload"));
    
    public static final LagStatusPacket PACKET = new LagStatusPacket();

    public LagStatusPacket() {}

    public static final StreamCodec<RegistryFriendlyByteBuf, LagStatusPacket> STREAM_CODEC = StreamCodec.unit(PACKET);

    public static void register() {
        PayloadTypeRegistry.serverboundPlay().register(TYPE, STREAM_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(TYPE, (payload, context) -> {
            PacketHandle.LagPacketHandleServerside(payload, context);
        });
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}