package com.example.lagprotection.config;

import com.example.lagprotection.client.ClothConfigScreen;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.network.chat.Component;

@Mod.EventBusSubscriber(modid = "lagprotection", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientCommands {

    @SubscribeEvent
    public static void onRegisterClientCommands(RegisterClientCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("lagprotection-config")
                .executes(ctx -> {
                    if (ModList.get().isLoaded("cloth_config")) {
                        Minecraft.getInstance().tell(() -> {
                            Minecraft.getInstance().setScreen(
                                    ClothConfigScreen.create(Minecraft.getInstance().screen)
                            );
                        });
                    } else {
                        if (Minecraft.getInstance().player == null) {
                            return 1;
                        }
                        Minecraft.getInstance().player.sendSystemMessage(Component.literal(
                                "Cloth Config is not installed on the client. Please install it to access configuration UI."
                        ));
                    }
                    return 1;
                })
        );
    }
}
