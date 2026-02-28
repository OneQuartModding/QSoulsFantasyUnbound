package net.onequart.qsouls.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.onequart.qsouls.client.input.ClientInputHandler;

public class SkillHudOverlay implements IGuiOverlay {

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui) return;
        
        int x = screenWidth / 2;
        int y = screenHeight / 2 + 15;

        float charge = ClientInputHandler.getChargeProgress();
        float cooldown = ClientInputHandler.getCooldownProgress();

        if (charge > 0) {
            int barWidth = 40;
            int barHeight = 3;
            int barX = x - barWidth / 2;
            int barY = y;

            guiGraphics.fill(barX, barY, barX + barWidth, barY + barHeight, 0xAA000000);

            int fillWidth = (int) (barWidth * charge);
            int color = charge >= 1.0f ? 0xFFFF3300 : 0xFFFFAA00;
            guiGraphics.fill(barX, barY, barX + fillWidth, barY + barHeight, color);
        }

        else if (cooldown > 0) {
            int cdWidth = 20;
            int cdHeight = 2;
            int cdX = x - cdWidth / 2;
            int cdY = y + 5;

            guiGraphics.fill(cdX, cdY, cdX + cdWidth, cdY + cdHeight, 0x55000000);

            int fillWidth = (int) (cdWidth * (1.0f - cooldown));
            guiGraphics.fill(cdX, cdY, cdX + fillWidth, cdY + cdHeight, 0x88FFFFFF);
        }

        else if (ClientInputHandler.isSkillReady()) {
            guiGraphics.fill(x - 1, y + 5, x + 1, y + 7, 0xAAFFFFFF);
        }
    }
}