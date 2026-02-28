package net.onequart.qsouls.client.input;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.onequart.qsouls.core.QSouls;
import net.onequart.qsouls.network.ModMessages;
import net.onequart.qsouls.network.StartDashC2SPacket;

@Mod.EventBusSubscriber(modid = QSouls.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientInputHandler {

    private static int dashCooldown = 0;
    private static int dashDuration = 0;

    private static int chargeTicks = 0;
    private static final int CHARGE_THRESHOLD = 15;
    private static final int MAX_COOLDOWN = 60;

    public static float getChargeProgress() {
        return Math.min(1.0f, (float) chargeTicks / CHARGE_THRESHOLD);
    }

    public static float getCooldownProgress() {
        return Math.max(0.0f, (float) dashCooldown / MAX_COOLDOWN);
    }

    public static boolean isSkillReady() {
        return dashCooldown <= 0;
    }

    @Mod.EventBusSubscriber(modid = QSouls.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(KeyBinds.CHARGE_ATTACK_KEY);
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null || mc.level == null) return;

        if (dashCooldown > 0) dashCooldown--;
        if (dashDuration > 0) dashDuration--;

        if (KeyBinds.CHARGE_ATTACK_KEY.isDown() && dashCooldown <= 0) {
            chargeTicks++;

            if (player.onGround()) {
                player.setDeltaMovement(player.getDeltaMovement().multiply(0.4D, 1.0D, 0.4D));
            }

            int particleCount = chargeTicks / 3 + 1;
            for (int i = 0; i < particleCount; i++) {
                double radius = 1.5 - ((double) chargeTicks / CHARGE_THRESHOLD);
                double angle = mc.level.random.nextDouble() * Math.PI * 2;

                double px = player.getX() + Math.cos(angle) * radius;
                double py = player.getY() + (mc.level.random.nextDouble() * 1.5);
                double pz = player.getZ() + Math.sin(angle) * radius;

                mc.level.addParticle(ParticleTypes.CRIT, px, py, pz, 0, 0, 0);

                if (chargeTicks > CHARGE_THRESHOLD / 2) {
                    mc.level.addParticle(ParticleTypes.ELECTRIC_SPARK, px, py, pz, 0, 0, 0);
                }
            }

            if (chargeTicks >= CHARGE_THRESHOLD) {
                Vec3 look = player.getLookAngle();
                Vec3 dashVector = new Vec3(look.x, 0, look.z).normalize().scale(1.8D);

                player.setDeltaMovement(dashVector.x, player.getDeltaMovement().y, dashVector.z);

                ModMessages.sendToServer(new StartDashC2SPacket());

                for (int i = 0; i < 20; i++) {
                    mc.level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                            player.getX(), player.getY() + 1, player.getZ(),
                            (mc.level.random.nextDouble() - 0.5) * 0.5,
                            0.1,
                            (mc.level.random.nextDouble() - 0.5) * 0.5);
                }

                dashCooldown = 60;
                dashDuration = 10;
                chargeTicks = 0;
            }
        } else {
            chargeTicks = 0;
        }

        if (dashDuration > 0) {
            mc.level.addParticle(ParticleTypes.SOUL_FIRE_FLAME,
                    player.getX(), player.getY() + 1.0, player.getZ(),
                    0, 0, 0);
        }
    }
}