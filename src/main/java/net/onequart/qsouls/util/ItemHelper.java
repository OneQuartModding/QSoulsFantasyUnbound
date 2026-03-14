package net.onequart.qsouls.util;

import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;
import net.onequart.qsouls.common.item.weapon.*;

public class ItemHelper {

    public static Vector3f getRarityColor(ItemStack stack) {

        if (stack.getItem() instanceof AzureNeedleItem ||
                stack.getItem() instanceof TitanRibItem ||
                stack.getItem() instanceof EchoingStingerItem ||
                stack.getItem() instanceof GaleHunterItem) {

            return new Vector3f(0.2f, 0.9f, 1.0f);
        }
        return null;
    }
}