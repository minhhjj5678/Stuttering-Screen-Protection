package com.example.lagprotection;

import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@EventBusSubscriber(modid = "lagprotection")
public class ServerEvents {

    private static final Set<ServerPlayer> healingQueue = ConcurrentHashMap.newKeySet();

    private static boolean isProtected(LivingEntity entity) {
        if (!(entity instanceof ServerPlayer player)) return false;
        return ServerProtectionState.isProtected(player);
    }

    @SubscribeEvent
    public static void onLivingIncomingDamageEvent(LivingIncomingDamageEvent event) {
        if (!isProtected(event.getEntity())) return;

        LivingEntity entity = event.getEntity();
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
    public static void onLivingDamageEventPre(LivingDamageEvent.Pre event) {
        if (!isProtected(event.getEntity())) return;
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        event.setNewDamage(0.0F);
        player.setAirSupply(player.getMaxAirSupply());
        player.clearFire();
        player.fallDistance = 0;
        player.connection.send(new ClientboundSetHealthPacket(
                player.getHealth(),
                player.getFoodData().getFoodLevel(),
                player.getFoodData().getSaturationLevel()
        ));
    }


    /*@SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!isProtected(player)) return;

        if (healingQueue.remove(player)) {
            player.setHealth(player.getMaxHealth());
        }
        player.setAirSupply(player.getMaxAirSupply());
        player.clearFire();
        player.fallDistance = 0;
        player.connection.send(new ClientboundSetHealthPacket(
                player.getHealth(),
                player.getFoodData().getFoodLevel(),
                player.getFoodData().getSaturationLevel()
        ));
    }*/
}
