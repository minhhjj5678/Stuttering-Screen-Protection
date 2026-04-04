package com.minhhjjj.stutterprotection.config;

import com.mojang.brigadier.arguments.BoolArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

public class ClientCommands {
    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal("stutterprotection-hud")
                .then(net.fabricmc.fabric.api.client.command.v2.ClientCommands.argument("active", BoolArgumentType.bool())
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