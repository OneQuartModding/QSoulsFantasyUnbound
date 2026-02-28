package net.onequart.qsouls.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.onequart.qsouls.core.QSouls;
import net.onequart.qsouls.client.gui.SkillHudOverlay;

@Mod.EventBusSubscriber(modid = QSouls.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetupEvents {

    @SubscribeEvent
    public static void onRegisterOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("skill_hud", new SkillHudOverlay());
    }
}