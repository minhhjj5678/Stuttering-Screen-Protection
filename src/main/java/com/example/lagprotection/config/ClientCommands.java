package com.example.lagprotection.config;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;

@EventBusSubscriber(modid = "lagprotection")
public class ClientCommands {

    @SubscribeEvent
    public static void onRegisterClientCommands(RegisterClientCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("lagprotection-hud")
            .then(Commands.argument("active", BoolArgumentType.bool())
                .executes(ctx -> {
                    boolean newValue = BoolArgumentType.getBool(ctx, "active");

                    LagConfig.SHOW_HUD.set(newValue);
                    LagConfig.COMMON_CONFIG.save();
                    
                    return 1;
                })
            )
        );
    }
}