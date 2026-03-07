package com.minhhjjj.stutterprotection;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

public class ServerEvents {
    public static void register() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            if (!isProtected(entity)) return true; 

            if (entity instanceof ServerPlayer serverPlayer) {
                serverPlayer.setAirSupply(serverPlayer.getMaxAirSupply());
                serverPlayer.clearFire();
                serverPlayer.resetFallDistance();
                serverPlayer.connection.send(new ClientboundSetHealthPacket(
                        serverPlayer.getHealth(),
                        serverPlayer.getFoodData().getFoodLevel(),
                        serverPlayer.getFoodData().getSaturationLevel()
                ));
                serverPlayer.hurtMarked = true;
            }
            return false; 
        });
    }

    private static boolean isProtected(LivingEntity entity) {
        if (!(entity instanceof ServerPlayer player)) return false;
        return ServerProtectionState.isProtected(player);
    }
}