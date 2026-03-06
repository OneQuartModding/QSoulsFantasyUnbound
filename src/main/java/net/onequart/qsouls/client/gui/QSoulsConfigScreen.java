package net.onequart.qsouls.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.onequart.qsouls.config.QSoulsClientConfig;

public class QSoulsConfigScreen extends Screen {
    private final Screen parent;

    public QSoulsConfigScreen(Screen parent) {
        super(Component.translatable("config.qsouls.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int startY = this.height / 6;
        int buttonWidth = 200;
        int buttonHeight = 20;
        int centerX = this.width / 2 - buttonWidth / 2;

        this.addRenderableWidget(CycleButton.onOffBuilder(QSoulsClientConfig.ENABLE_IMPACT_FRAMES.get())
                .create(centerX, startY, buttonWidth, buttonHeight,
                        Component.translatable("config.qsouls.enable_impact_frames"),
                        (button, newValue) -> QSoulsClientConfig.ENABLE_IMPACT_FRAMES.set(newValue))); // Сразу сохраняем в конфиг

        this.addRenderableWidget(CycleButton.onOffBuilder(QSoulsClientConfig.ENABLE_SCREEN_SHAKE.get())
                .create(centerX, startY + 25, buttonWidth, buttonHeight,
                        Component.translatable("config.qsouls.enable_screen_shake"),
                        (button, newValue) -> QSoulsClientConfig.ENABLE_SCREEN_SHAKE.set(newValue)));

        this.addRenderableWidget(new ShakeIntensitySlider(centerX, startY + 50, buttonWidth, buttonHeight));

        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, (btn) -> this.minecraft.setScreen(this.parent))
                .bounds(centerX, this.height - 40, buttonWidth, buttonHeight)
                .build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 15, 0xFFFFFF);

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    private static class ShakeIntensitySlider extends AbstractSliderButton {
        private final double min = 0.0;
        private final double max = 3.0;

        public ShakeIntensitySlider(int x, int y, int width, int height) {
            super(x, y, width, height, Component.empty(),
                    (QSoulsClientConfig.SCREEN_SHAKE_INTENSITY.get() - 0.0) / (3.0 - 0.0));
            this.updateMessage();
        }

        @Override
        protected void updateMessage() {
            double actualValue = min + (max - min) * this.value;
            this.setMessage(Component.translatable("config.qsouls.screen_shake_intensity")
                    .append(": " + String.format("%.1f", actualValue)));
        }

        @Override
        protected void applyValue() {
            double actualValue = min + (max - min) * this.value;
            QSoulsClientConfig.SCREEN_SHAKE_INTENSITY.set(actualValue);
        }
    }
}