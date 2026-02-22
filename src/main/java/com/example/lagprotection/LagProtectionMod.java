package com.example.lagprotection;

import com.example.lagprotection.config.LagConfig;
import com.example.lagprotection.network.PacketHandle;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("lagprotection")
public class LagProtectionMod {
    public static final String MODID = "lagprotection";

    public LagProtectionMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCommonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, LagConfig.COMMON_CONFIG);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        PacketHandle.init();
    }


    private void onClientSetup(FMLClientSetupEvent event) {
        com.example.lagprotection.client.FpsChecker.start();
    }
}
