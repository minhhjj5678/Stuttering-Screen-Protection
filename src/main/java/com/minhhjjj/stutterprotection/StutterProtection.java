package com.minhhjjj.stutterprotection;

import com.minhhjjj.stutterprotection.config.LagConfig;
import com.minhhjjj.stutterprotection.network.PacketHandle;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("stutterprotection")
public class StutterProtection {
    public static final String MODID = "stutterprotection";

    @SuppressWarnings("removal")
    public StutterProtection() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCommonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, LagConfig.CLIENT_CONFIG);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            PacketHandle.init();
        });
    }


    private void onClientSetup(FMLClientSetupEvent event) {
        //com.example.lagprotection.client.FpsChecker.start();
    }
}