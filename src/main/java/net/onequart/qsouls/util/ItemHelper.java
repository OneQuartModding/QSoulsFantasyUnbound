package net.onequart.qsouls.util;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.joml.Vector3f;

public class ItemHelper {

    public static Vector3f getRarityColor(ItemStack stack) {
        Rarity rarity = stack.getRarity();

        if (rarity == Rarity.UNCOMMON) {
            return new Vector3f(1.0f, 1.0f, 0.33f);
        } else if (rarity == Rarity.RARE) {
            return new Vector3f(0.33f, 1.0f, 1.0f);
        } else if (rarity == Rarity.EPIC) {
            return new Vector3f(1.0f, 0.33f, 1.0f);
        }

        return null;
    }
}