package com.minhhjjj.stutterprotection.config;

import com.mojang.brigadier.arguments.BoolArgumentType;
import net.minecraft.client.Minecraft;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientCommands {

    @SuppressWarnings("null")
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(
            Commands.literal("stutterprotection-hud")
                .then(Commands.argument("trang_thai", BoolArgumentType.bool())
                    .executes(ctx -> {
                        boolean showHud = BoolArgumentType.getBool(ctx, "trang_thai");
                        
                        // Cập nhật config
                        LagConfig.SHOW_HUD.set(showHud);
                        
                        // Thông báo cho người chơi
                        if (Minecraft.getInstance().player != null) {
                            Minecraft.getInstance().player.sendMessage(
                                new StringTextComponent("§a[Stutter Protection] §fHUD set to: §e" + showHud),
                                Minecraft.getInstance().player.getUUID()
                            );
                        }
                        return 1;
                    })
                )
        );
    }
}