package com.example.lagprotection;

import com.example.lagprotection.network.ClearEffectPacket;
import com.example.lagprotection.network.PacketHandle;
import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.living.MobEffectEvent.Added;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = "lagprotection", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerEvents {

    private static boolean isProtected(LivingEntity entity) {
        if (!(entity instanceof ServerPlayer player)) return false;
        return ServerProtectionState.isProtected(player);
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (!isProtected(event.getEntity())) return;

        String sourceId = event.getSource().getMsgId();
        LivingEntity entity = event.getEntity();

        if ("drown".equals(sourceId) && entity instanceof Player player) {
            player.setAirSupply(player.getMaxAirSupply());
        }

        if (entity instanceof ServerPlayer serverPlayer) {
            serverPlayer.connection.send(new ClientboundSetHealthPacket(
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
        if (!isProtected(event.getEntity())) return;
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        if (!isProtected(event.getEntity())) return;

        LivingEntity entity = event.getEntity();

        if (entity instanceof Player player) {
            player.clearFire();
            player.setDeltaMovement(0, 0, 0);
            player.fallDistance = 0;
        }

        event.setCanceled(true);
    }

    //@SubscribeEvent
    public static void onPotionApply(MobEffectEvent.Added event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!ServerProtectionState.isProtected(player)) return;

        if (event.getEffectSource() instanceof AreaEffectCloud || event.getEffectSource() instanceof ThrownPotion) return;

        if (event.getEffectInstance().getEffect() == MobEffects.DARKNESS ||
                event.getEffectInstance().getEffect() == MobEffects.DIG_SLOWDOWN) {
            return;
        }

        if (event.getEffectSource() == null || event.getEffectSource() == event.getEntity()) return;

        //player.removeEffect(event.getEffectInstance().getEffect());
        /*if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            PacketHandle.INSTANCE.send(
                    PacketDistributor.PLAYER.with(() -> serverPlayer),
                    new ClearEffectPacket(event.getEffectInstance().getEffect())
            );
        }*/
    }
}
