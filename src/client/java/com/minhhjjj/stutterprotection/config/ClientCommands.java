package com.minhhjjj.stutterprotection.config;

import com.mojang.brigadier.arguments.BoolArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.network.chat.Component;

public class ClientCommands {
    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("stutterprotection-hud")
                .then(ClientCommandManager.argument("active", BoolArgumentType.bool())
                    .executes(ctx -> {
                        boolean newValue = BoolArgumentType.getBool(ctx, "active");
                        LagConfig.SHOW_HUD = newValue;
                        LagConfig.save();
                        return 1;
                    })
                )
            );
        });
    }
}