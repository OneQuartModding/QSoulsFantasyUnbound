package net.onequart.qsouls.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;

public class CustomBeamRenderer {

    public static void renderLightBeam(PoseStack poseStack, float r, float g, float b, float height, float radius, float alphaMax) {
        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        poseStack.pushPose();

        Matrix4f matrix = poseStack.last().pose();
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();

        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        int segments = 8;
        for (int i = 0; i < segments; ++i) {
            float angle1 = (float)(i * Math.PI * 2.0D / (double)segments);
            float angle2 = (float)((i + 1) * Math.PI * 2.0D / (double)segments);

            float x1 = (float)Math.sin(angle1) * radius;
            float z1 = (float)Math.cos(angle1) * radius;
            float x2 = (float)Math.sin(angle2) * radius;
            float z2 = (float)Math.cos(angle2) * radius;

            bufferbuilder.vertex(matrix, x1, 0.0F, z1).color(r, g, b, alphaMax).endVertex();
            bufferbuilder.vertex(matrix, x2, 0.0F, z2).color(r, g, b, alphaMax).endVertex();
            bufferbuilder.vertex(matrix, x2, height, z2).color(r, g, b, 0.0F).endVertex();
            bufferbuilder.vertex(matrix, x1, height, z1).color(r, g, b, 0.0F).endVertex();
        }

        tesselator.end();

        poseStack.popPose();
        RenderSystem.disableBlend();
        RenderSystem.enableCull();
    }
}