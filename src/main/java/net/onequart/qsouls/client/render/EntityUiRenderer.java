package net.onequart.qsouls.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.onequart.qsouls.core.QSouls;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Mod.EventBusSubscriber(modid = QSouls.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class EntityUiRenderer {

    private static final ResourceLocation HP_BAR_BG = new ResourceLocation(QSouls.MOD_ID, "textures/gui/hp_bar_bg.png");
    private static final ResourceLocation HP_BAR_FILL = new ResourceLocation(QSouls.MOD_ID, "textures/gui/hp_bar_fill.png");

    private static final List<DamageText> activeDamageTexts = new ArrayList<>();

    public static void addDamageNumber(Entity entity, float damage) {
        double xOffset = (Math.random() - 0.5) * 1.0;
        double zOffset = (Math.random() - 0.5) * 1.0;
        Vec3 startPos = entity.position().add(xOffset, entity.getBbHeight() + 0.5, zOffset);

        String text = (damage % 1 == 0) ? String.valueOf((int) damage) : String.format("%.1f", damage);
        activeDamageTexts.add(new DamageText(text, startPos, 0xFFFF5555));
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Iterator<DamageText> iterator = activeDamageTexts.iterator();
            while (iterator.hasNext()) {
                DamageText dt = iterator.next();
                dt.tick();
                if (dt.age > dt.maxAge) iterator.remove();
            }
        }
    }

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES) return;

        Minecraft mc = Minecraft.getInstance();
        PoseStack poseStack = event.getPoseStack();
        Vec3 cameraPos = event.getCamera().getPosition();
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
        Font font = mc.font;

        for (DamageText dt : activeDamageTexts) {
            double x = dt.pos.x - cameraPos.x;
            double y = (dt.pos.y + (dt.getVerticalOffset() * event.getPartialTick())) - cameraPos.y;
            double z = dt.pos.z - cameraPos.z;

            poseStack.pushPose();
            poseStack.translate(x, y, z);
            poseStack.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());
            poseStack.scale(-0.025F, -0.025F, 0.025F);

            int alpha = (int) (255 * (1.0f - ((float) dt.age / dt.maxAge)));
            alpha = Math.max(4, alpha);
            int color = (dt.color & 0x00FFFFFF) | (alpha << 24);

            Matrix4f matrix4f = poseStack.last().pose();
            float textX = (float) (-font.width(dt.text) / 2);

            font.drawInBatch(dt.text, textX, 0, color, false, matrix4f, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
            poseStack.popPose();
        }
        bufferSource.endBatch();
    }

    @SubscribeEvent
    public static void onRenderLiving(RenderLivingEvent.Post<?, ?> event) {
        LivingEntity entity = event.getEntity();
        Minecraft mc = Minecraft.getInstance();

        if (entity == mc.player || entity.isInvisible() || entity.getHealth() >= entity.getMaxHealth()) return;
        if (entity.distanceToSqr(mc.player) > 400) return;

        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource bufferSource = event.getMultiBufferSource();

        poseStack.pushPose();
        poseStack.translate(0.0D, entity.getBbHeight() + 0.6D, 0.0D);
        poseStack.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());

        poseStack.scale(-0.025F, -0.025F, 0.025F);

        Matrix4f matrix4f = poseStack.last().pose();

        float healthPct = entity.getHealth() / entity.getMaxHealth();

        float width = 40.0f;
        float height = 6.0f;

        drawTexturedQuad(matrix4f, bufferSource, HP_BAR_BG, width, height, 1.0f);

        drawTexturedQuad(matrix4f, bufferSource, HP_BAR_FILL, width, height, healthPct);

        poseStack.popPose();
    }

    private static void drawTexturedQuad(Matrix4f matrix, MultiBufferSource buffer, ResourceLocation texture, float width, float height, float maxU) {
        VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.entityTranslucent(texture));

        int light = 15728880;

        float minX = -width / 2;
        float maxX = minX + (width * maxU);
        float minY = 0;
        float maxY = height;

        float minU = 0;
        float maxU_tex = maxU;
        float minV = 0;
        float maxV = 1;

        vertexconsumer.vertex(matrix, minX, minY, 0.0F).color(255, 255, 255, 255).uv(minU, maxV)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0.0F, 1.0F, 0.0F).endVertex();

        vertexconsumer.vertex(matrix, maxX, minY, 0.0F).color(255, 255, 255, 255).uv(maxU_tex, maxV)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0.0F, 1.0F, 0.0F).endVertex();

        vertexconsumer.vertex(matrix, maxX, maxY, 0.0F).color(255, 255, 255, 255).uv(maxU_tex, minV)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0.0F, 1.0F, 0.0F).endVertex();

        vertexconsumer.vertex(matrix, minX, maxY, 0.0F).color(255, 255, 255, 255).uv(minU, minV)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(0.0F, 1.0F, 0.0F).endVertex();
    }

    private static class DamageText {
        String text; Vec3 pos; int color; int age = 0; int maxAge = 30;
        DamageText(String text, Vec3 pos, int color) { this.text = text; this.pos = pos; this.color = color; }
        void tick() { age++; pos = pos.add(0, 0.03, 0); }
        float getVerticalOffset() { return 0.03f; }
    }
}