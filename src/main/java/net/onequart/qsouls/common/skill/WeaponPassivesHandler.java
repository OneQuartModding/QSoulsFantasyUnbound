package net.onequart.qsouls.common.skill;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.onequart.qsouls.QSouls;
import net.onequart.qsouls.common.item.weapon.GaleHunterItem;

@Mod.EventBusSubscriber(modid = QSouls.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class WeaponPassivesHandler {

    @SubscribeEvent
    public static void onArrowShot(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof AbstractArrow arrow) {
            if (arrow.getOwner() instanceof LivingEntity shooter) {
                if (shooter.getMainHandItem().getItem() instanceof GaleHunterItem ||
                        shooter.getOffhandItem().getItem() instanceof GaleHunterItem) {

                    arrow.addTag("qsouls_gale_arrow");
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEntityHurt(LivingHurtEvent event) {
        if (event.getSource().getDirectEntity() instanceof net.minecraft.world.entity.projectile.AbstractArrow arrow) {
            if (arrow.getTags().contains("qsouls_gale_arrow")) {
                event.getEntity().addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.MOVEMENT_SLOWDOWN, 60, 1));

                if (!event.getEntity().level().isClientSide) {
                    net.minecraft.server.level.ServerLevel serverLevel = (net.minecraft.server.level.ServerLevel) event.getEntity().level();
                    serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.CLOUD,
                            event.getEntity().getX(), event.getEntity().getY() + 1.0, event.getEntity().getZ(),
                            10, 0.3, 0.3, 0.3, 0.05);
                }
            }
        }
        if (event.getSource().getDirectEntity() instanceof net.minecraft.world.entity.player.Player player) {
            if (player.getMainHandItem().getItem() instanceof net.onequart.qsouls.common.item.weapon.EchoingStingerItem) {

                double distance = player.distanceTo(event.getEntity());

                if (distance >= 3.2D) {
                    event.setAmount(event.getAmount() * 1.5F);

                    if (!player.level().isClientSide && player.level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
                        serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.CRIT,
                                event.getEntity().getX(), event.getEntity().getY() + 1.0, event.getEntity().getZ(),
                                15, 0.2, 0.2, 0.2, 0.1);
                        player.level().playSound(null, event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(),
                                net.minecraft.sounds.SoundEvents.TRIDENT_HIT, net.minecraft.sounds.SoundSource.PLAYERS, 1.0F, 1.5F);
                    }
                }
            }
        }
    }
}