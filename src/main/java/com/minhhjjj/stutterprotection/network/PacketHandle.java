package com.minhhjjj.stutterprotection.network;

import com.minhhjjj.stutterprotection.StutterProtection;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandle {
    private static final String PROTOCOL_VERSION = "1.0";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(StutterProtection.MODID, "main"),
            () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals
    );

    public static void init() {
        INSTANCE.registerMessage(0, LagStatusPacket.class, LagStatusPacket::encode, LagStatusPacket::decode, LagStatusPacket::handle);
    }
}
