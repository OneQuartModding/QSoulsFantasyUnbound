package net.onequart.qsouls.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.onequart.qsouls.QSouls;
import net.onequart.qsouls.config.QSoulsClientConfig;

@Mod.EventBusSubscriber(modid = QSouls.MOD_ID, value = Dist.CLIENT)
public class ImpactFrameManager {

    private static int activeTicks = 0;
    private static int maxTicks = 0;
    private static int color = 0xFFFFFFFF;
    private static boolean invertColors = false;

    public static void trigger(int ticks, int argbColor) {
        if (!QSoulsClientConfig.ENABLE_IMPACT_FRAMES.get()) return; // Проверка конфига
        activeTicks = ticks;
        maxTicks = ticks;
        color = argbColor;
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && activeTicks > 0) {
            activeTicks--;
        }
    }

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        if (activeTicks > 0) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level == null) return;

            GuiGraphics guiGraphics = event.getGuiGraphics();
            int width = mc.getWindow().getGuiScaledWidth();
            int height = mc.getWindow().getGuiScaledHeight();

            guiGraphics.fill(0, 0, width, height, color);

            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}