package net.onequart.qsouls.client.input;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBinds {
    public static final String KEY_CATEGORY_QSOULS = "key.category.qsouls";
    public static final String KEY_CHARGE_ATTACK = "key.qsouls.charge_attack";

    public static final KeyMapping CHARGE_ATTACK_KEY = new KeyMapping(
            KEY_CHARGE_ATTACK,
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            KEY_CATEGORY_QSOULS
    );
}