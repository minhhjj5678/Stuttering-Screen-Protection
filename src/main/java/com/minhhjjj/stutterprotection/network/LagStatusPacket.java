package com.minhhjjj.stutterprotection.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;


public class LagStatusPacket implements CustomPacketPayload {
    @SuppressWarnings("null")
    public static final CustomPacketPayload.Type<LagStatusPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("stutterprotection","payload"));
	public static final LagStatusPacket PACKET = new LagStatusPacket();

    public LagStatusPacket() {}

    @SuppressWarnings("null")
    public static final StreamCodec<ByteBuf, LagStatusPacket> STREAM_CODEC = StreamCodec.unit(PACKET);

    @SuppressWarnings("null")
    @SubscribeEvent
	public static void register(final RegisterPayloadHandlersEvent event) {
		final PayloadRegistrar registrar = event.registrar("1");
	
		registrar.playToServer(
			LagStatusPacket.TYPE,
			LagStatusPacket.STREAM_CODEC,
			PacketHandle::LagPacketHandleServerside
		);
}

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
