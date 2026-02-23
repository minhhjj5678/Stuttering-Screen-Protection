package com.minhhjjj.stutterprotection.config;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.minecraftforge.api.distmarker.Dist;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientCommands {

    @SuppressWarnings("null")
    @SubscribeEvent
    public static void onRegisterClientCommands(RegisterClientCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("stutterprotection-hud")
                .then(
                    Commands.argument("trang_thai", BoolArgumentType.bool())
                        .executes(ctx -> {
                            if (Minecraft.getInstance().player == null) {
                                return 0;
                            }
                            boolean showFps = BoolArgumentType.getBool(ctx, "trang_thai");
                            LagConfig.SHOW_HUD.set(showFps);
                            return 1;
                        })
                    
                )
        );
    }
}
