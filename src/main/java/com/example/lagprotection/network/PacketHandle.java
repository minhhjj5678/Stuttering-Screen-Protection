package com.example.lagprotection.network;

import com.example.lagprotection.LagProtectionMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandle {
    private static final String PROTOCOL_VERSION = "1.0";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(LagProtectionMod.MODID, "main"),
            () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals
    );

    public static void init() {
        INSTANCE.registerMessage(0, LagStatusPacket.class, LagStatusPacket::encode, LagStatusPacket::decode, LagStatusPacket::handle);
        INSTANCE.registerMessage(
                1,
                ClearEffectPacket.class,
                ClearEffectPacket::encode,
                ClearEffectPacket::decode,
                ClearEffectPacket::handle
        );

    }
}
