package com.example.lagprotection.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import net.minecraft.client.Minecraft;

import java.util.function.Supplier;

public class ClearEffectPacket {
    private final MobEffect effect;

    public ClearEffectPacket(MobEffect effect) {
        this.effect = effect;
    }

    public static void encode(ClearEffectPacket packet, FriendlyByteBuf buf) {
        buf.writeVarInt(MobEffect.getId(packet.effect)); // Serialize effect ID
    }

    public static ClearEffectPacket decode(FriendlyByteBuf buf) {
        int id = buf.readVarInt();
        MobEffect effect = MobEffect.byId(id);
        return new ClearEffectPacket(effect);
    }

    public static void handle(ClearEffectPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            removeEffectClient(packet.effect);
        });
        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void removeEffectClient(MobEffect effect) {
        var player = Minecraft.getInstance().player;
        if (player != null) {
            player.removeEffect(effect);
        }
    }
}