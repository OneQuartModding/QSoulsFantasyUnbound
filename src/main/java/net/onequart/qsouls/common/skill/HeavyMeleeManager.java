package net.onequart.qsouls.common.skill;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.onequart.qsouls.core.QSouls;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

@Mod.EventBusSubscriber(modid = QSouls.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HeavyMeleeManager {

    private static final Map<Player, DashData> ACTIVE_DASHES = new WeakHashMap<>();

    public static void startDash(ServerPlayer player) {
        ACTIVE_DASHES.put(player, new DashData(10));

        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 0.5F);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide) return;

        Player player = event.player;
        DashData dashData = ACTIVE_DASHES.get(player);

        if (dashData != null) {
            dashData.ticksLeft--;

            if (!player.onGround()) {
                player.setDeltaMovement(player.getDeltaMovement().x, 0, player.getDeltaMovement().z);
                player.hurtMarked = true;
            }

            AABB hitBox = player.getBoundingBox().inflate(0.8D);

            for (Entity entity : player.level().getEntities(player, hitBox)) {
                if (entity instanceof LivingEntity target && !dashData.hitEntities.contains(target)) {

                    double baseDamage = player.getAttributeValue(Attributes.ATTACK_DAMAGE);
                    float totalDamage = (float) (baseDamage * 1.8);

                    boolean hurt = target.hurt(player.damageSources().playerAttack(player), totalDamage);

                    if (hurt) {
                        dashData.hitEntities.add(target);

                        target.knockback(0.8F, Math.sin(player.getYRot() * (Math.PI / 180F)), -Math.cos(player.getYRot() * (Math.PI / 180F)));

                        player.level().playSound(null, target.getX(), target.getY(), target.getZ(),
                                SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.PLAYERS, 1.0F, 0.8F);
                        player.level().playSound(null, target.getX(), target.getY(), target.getZ(),
                                SoundEvents.IRON_GOLEM_DAMAGE, SoundSource.PLAYERS, 0.8F, 1.2F);
                    }
                }
            }

            if (dashData.ticksLeft <= 0) {
                ACTIVE_DASHES.remove(player);
            }
        }
    }

    private static class DashData {
        int ticksLeft;
        Set<Entity> hitEntities = new HashSet<>();

        DashData(int ticksLeft) {
            this.ticksLeft = ticksLeft;
        }
    }
}