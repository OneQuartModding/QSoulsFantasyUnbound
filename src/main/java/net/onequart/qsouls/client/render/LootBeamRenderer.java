package net.onequart.qsouls.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.onequart.qsouls.core.QSouls;
import net.onequart.qsouls.util.ItemHelper;
import org.joml.Vector3f;

@Mod.EventBusSubscriber(modid = QSouls.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class LootBeamRenderer {

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;

        PoseStack poseStack = event.getPoseStack();
        Vec3 cameraPos = event.getCamera().getPosition();
        float partialTick = event.getPartialTick();

        for (Entity entity : mc.level.entitiesForRendering()) {
            if (entity instanceof ItemEntity itemEntity) {
                Vector3f color = ItemHelper.getRarityColor(itemEntity.getItem());
                if (color == null) continue;

                double x = (itemEntity.xOld + (itemEntity.getX() - itemEntity.xOld) * partialTick) - cameraPos.x;
                double y = (itemEntity.yOld + (itemEntity.getY() - itemEntity.yOld) * partialTick) - cameraPos.y;
                double z = (itemEntity.zOld + (itemEntity.getZ() - itemEntity.zOld) * partialTick) - cameraPos.z;

                poseStack.pushPose();
                poseStack.translate(x, y + 0.1, z);

                CustomBeamRenderer.renderLightBeam(
                        poseStack,
                        color.x(), color.y(), color.z(),
                        2.0F,
                        0.1F,
                        0.6F
                );

                poseStack.popPose();
            }
        }
    }
}