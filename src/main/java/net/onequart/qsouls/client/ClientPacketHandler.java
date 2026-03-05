package net.onequart.qsouls.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.onequart.qsouls.client.render.EntityUiRenderer;

public class ClientPacketHandler {
    public static void handleDamageNumber(int entityId, float damage) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null) {
            Entity entity = mc.level.getEntity(entityId);
            if (entity != null) {
                EntityUiRenderer.addDamageNumber(entity, damage);
            }
        }
    }
}