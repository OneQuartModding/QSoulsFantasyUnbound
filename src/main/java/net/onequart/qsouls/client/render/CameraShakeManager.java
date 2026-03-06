package net.onequart.qsouls.client.render;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.onequart.qsouls.QSouls;
import net.onequart.qsouls.config.QSoulsClientConfig;

import java.util.Random;

@Mod.EventBusSubscriber(modid = QSouls.MOD_ID, value = Dist.CLIENT)
public class CameraShakeManager {

    private static int activeTicks = 0;
    private static int maxTicks = 0;
    private static float currentIntensity = 0f;
    private static final Random random = new Random();

    public static void trigger(int ticks, float intensity) {
        if (!QSoulsClientConfig.ENABLE_SCREEN_SHAKE.get()) return;

        float configMultiplier = QSoulsClientConfig.SCREEN_SHAKE_INTENSITY.get().floatValue();

        activeTicks = ticks;
        maxTicks = ticks;
        currentIntensity = intensity * configMultiplier;
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && activeTicks > 0) {
            activeTicks--;
        }
    }

    @SubscribeEvent
    public static void onCameraSetup(ViewportEvent.ComputeCameraAngles event) {
        if (activeTicks > 0) {
            float progress = (float) activeTicks / maxTicks;
            float shakeAmount = currentIntensity * progress;

            float yawOffset = (random.nextFloat() - 0.5f) * 2f * shakeAmount;
            float pitchOffset = (random.nextFloat() - 0.5f) * 2f * shakeAmount;
            float rollOffset = (random.nextFloat() - 0.5f) * 2f * shakeAmount;

            event.setYaw(event.getYaw() + yawOffset);
            event.setPitch(event.getPitch() + pitchOffset);
            event.setRoll(event.getRoll() + rollOffset);
        }
    }
}