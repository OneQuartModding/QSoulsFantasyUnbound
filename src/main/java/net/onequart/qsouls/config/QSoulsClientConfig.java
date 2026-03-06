package net.onequart.qsouls.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class QSoulsClientConfig {
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue ENABLE_IMPACT_FRAMES;

    public static final ForgeConfigSpec.BooleanValue ENABLE_SCREEN_SHAKE;
    public static final ForgeConfigSpec.DoubleValue SCREEN_SHAKE_INTENSITY;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("visual_effects");

        ENABLE_IMPACT_FRAMES = builder
                .comment("Enable impact frames (screen flashes) on heavy hits")
                .translation("config.qsouls.enable_impact_frames")
                .define("enableImpactFrames", true);

        ENABLE_SCREEN_SHAKE = builder
                .comment("Enable camera shake on heavy hits")
                .translation("config.qsouls.enable_screen_shake")
                .define("enableScreenShake", true);

        SCREEN_SHAKE_INTENSITY = builder
                .comment("Multiplier for screen shake intensity")
                .translation("config.qsouls.screen_shake_intensity")
                .defineInRange("screenShakeIntensity", 1.0, 0.0, 3.0); // От 0 до 3х

        builder.pop();

        SPEC = builder.build();
    }
}