package com.minhhjjj.stutterprotection;

import net.minecraft.network.play.server.SUpdateHealthPacket;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = StutterProtection.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerEvents {

    private static boolean isProtected(LivingEntity entity) {
        if (!(entity instanceof ServerPlayerEntity)) return false;
        ServerPlayerEntity player = (ServerPlayerEntity) entity;
        return ServerProtectionState.isProtected(player);
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (!isProtected(event.getEntityLiving())) return;

        String sourceId = event.getSource().getMsgId();
        LivingEntity entity = event.getEntityLiving();

        if ("drown".equals(sourceId) && entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            player.setAirSupply(player.getMaxAirSupply());
        }

        if (entity instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) entity;
            serverPlayer.connection.send(new SUpdateHealthPacket(
                    serverPlayer.getHealth(),
                    serverPlayer.getFoodData().getFoodLevel(),
                    serverPlayer.getFoodData().getSaturationLevel()
            ));
            serverPlayer.hurtMarked = true;
        }

        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        if (!isProtected(event.getEntityLiving())) return;
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        if (!isProtected(event.getEntityLiving())) return;

        LivingEntity entity = event.getEntityLiving();

        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            player.clearFire();
            // player.setDeltaMovement(0, 0, 0);
            player.fallDistance = 0;
        }

        event.setCanceled(true);
    }
}
