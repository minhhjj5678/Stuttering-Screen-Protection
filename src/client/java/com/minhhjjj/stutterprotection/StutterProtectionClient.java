package com.minhhjjj.stutterprotection;

import com.minhhjjj.stutterprotection.client.ClientHudRenderer;
import com.minhhjjj.stutterprotection.config.LagConfig;
import com.minhhjjj.stutterprotection.config.ClientCommands;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import com.minhhjjj.stutterprotection.client.FpsChecker;

public class StutterProtectionClient implements ClientModInitializer {
    
    @Override
    public void onInitializeClient() {
		LagConfig.load();

        HudRenderCallback.EVENT.register((guiGraphics, partialTick) -> {
            FpsChecker.onRenderFrame();
            ClientHudRenderer.render(guiGraphics, partialTick);
        });
		ClientCommands.register();
    }
}