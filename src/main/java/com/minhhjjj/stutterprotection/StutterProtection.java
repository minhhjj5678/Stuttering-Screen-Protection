package com.minhhjjj.stutterprotection;

import net.fabricmc.api.ModInitializer;
import com.minhhjjj.stutterprotection.network.LagStatusPacket;

public class StutterProtection implements ModInitializer {
    public static final String MODID = "stutterprotection";

    @Override
    public void onInitialize() {
        LagStatusPacket.register();
        ServerEvents.register();
		ServerProtectionState.register();
    }
}