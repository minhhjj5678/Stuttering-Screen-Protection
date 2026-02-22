package com.example.lagprotection;

import com.example.lagprotection.client.ClientHudRenderer;
import com.example.lagprotection.config.LagConfig;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

@Mod("lagprotection")
public class LagProtectionMod {
    public static final String MODID = "lagprotection";

    public LagProtectionMod(ModContainer container) {
        IEventBus modEventBus = container.getEventBus();
        modEventBus.addListener(com.example.lagprotection.network.LagStatusPacket::register);
        modEventBus.addListener(this::registerGuiLayers);
        container.registerConfig(ModConfig.Type.COMMON, LagConfig.COMMON_CONFIG);
    }
	
    private void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAboveAll(
            net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(MODID, "fps_hud"), 
            ClientHudRenderer::render 
        );
    }
}