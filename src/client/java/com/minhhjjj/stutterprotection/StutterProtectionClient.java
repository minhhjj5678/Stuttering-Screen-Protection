package com.minhhjjj.stutterprotection;

import com.minhhjjj.stutterprotection.client.ClientHudRenderer;
import com.minhhjjj.stutterprotection.config.LagConfig;
import com.minhhjjj.stutterprotection.config.ClientCommands;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.resources.Identifier;
import com.minhhjjj.stutterprotection.client.FpsChecker;

public class StutterProtectionClient implements ClientModInitializer {
    
    @Override
    public void onInitializeClient() {
		LagConfig.load();

        HudElementRegistry.attachElementAfter(
            VanillaHudElements.MISC_OVERLAYS,
            Identifier.fromNamespaceAndPath(StutterProtection.MODID, "hud"),
            (guiGraphics, partialTick) -> {
            FpsChecker.onRenderFrame();
            ClientHudRenderer.render(guiGraphics, partialTick);
            }
        );
		ClientCommands.register();
    }
}