package com.example.lagprotection.network;

import com.example.lagprotection.ServerProtectionState;
import com.google.common.eventbus.Subscribe;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.fml.common.EventBusSubscriber;


public class LagStatusPacket implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<LagStatusPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("lagprotection","payload"));
	public static final LagStatusPacket PACKET = new LagStatusPacket();

    public LagStatusPacket() {}

    public static final StreamCodec<ByteBuf, LagStatusPacket> STREAM_CODEC = StreamCodec.unit(PACKET);

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
