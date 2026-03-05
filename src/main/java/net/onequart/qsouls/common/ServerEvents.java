package net.onequart.qsouls.common;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.onequart.qsouls.core.QSouls;
import net.onequart.qsouls.network.DamageNumberS2CPacket;
import net.onequart.qsouls.network.ModMessages;

@Mod.EventBusSubscriber(modid = QSouls.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerEvents {

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        LivingEntity entity = event.getEntity();
        float damage = event.getAmount();

        if (!entity.level().isClientSide && damage > 0) {
            ModMessages.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity),
                    new DamageNumberS2CPacket(entity.getId(), damage));
        }
    }
}