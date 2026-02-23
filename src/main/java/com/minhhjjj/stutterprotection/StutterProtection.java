package com.minhhjjj.stutterprotection;

import com.minhhjjj.stutterprotection.client.ClientHudRenderer;
import com.minhhjjj.stutterprotection.config.LagConfig;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

@Mod("stutterprotection")
public class StutterProtection {
    public static final String MODID = "stutterprotection";

    public StutterProtection(ModContainer container) {
        IEventBus modEventBus = container.getEventBus();
        modEventBus.addListener(com.minhhjjj.stutterprotection.network.LagStatusPacket::register);
        modEventBus.addListener(this::registerGuiLayers);
        container.registerConfig(ModConfig.Type.COMMON, LagConfig.COMMON_CONFIG);
    }
	
    @SuppressWarnings("null")
    private void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAboveAll(
            net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(MODID, "fps_hud"), 
            ClientHudRenderer::render 
        );
    }
}