package net.onequart.qsouls.common.item.weapon;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class EchoingStingerItem extends SwordItem {
    private final Multimap<Attribute, AttributeModifier> customAttributes;

    public EchoingStingerItem(Properties properties) {
        super(Tiers.IRON, 4, -2.2f, properties);

        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(super.getDefaultAttributeModifiers(EquipmentSlot.MAINHAND));
        builder.put(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(
                UUID.fromString("1f2a3c4d-5e6f-7a8b-9c0d-1e2f3a4b5c6d"),
                "Weapon reach", 1.5D, AttributeModifier.Operation.ADDITION));

        this.customAttributes = builder.build();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? this.customAttributes : super.getDefaultAttributeModifiers(slot);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("★★★ ").withStyle(ChatFormatting.AQUA)
                .append(Component.translatable("tooltip.qsouls.type.polearm")));
        tooltip.add(Component.empty());
        tooltip.add(Component.translatable("item.qsouls.echoing_stinger.lore").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        tooltip.add(Component.empty());
        tooltip.add(Component.translatable("tooltip.qsouls.passive").withStyle(ChatFormatting.GOLD)
                .append(Component.translatable("item.qsouls.echoing_stinger.passive_name").withStyle(ChatFormatting.WHITE)));
        tooltip.add(Component.translatable("item.qsouls.echoing_stinger.passive_desc").withStyle(ChatFormatting.GREEN));
    }

    @Override
    public net.minecraft.network.chat.Component getName(ItemStack stack) {
        return super.getName(stack).copy().withStyle(net.minecraft.ChatFormatting.AQUA);
    }

    @Override
    public void inventoryTick(ItemStack stack, net.minecraft.world.level.Level level, net.minecraft.world.entity.Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);

        if (isSelected && level.isClientSide && entity instanceof net.minecraft.world.entity.player.Player player) {
            if (level.getGameTime() % 3 == 0) {
                net.minecraft.world.phys.Vec3 look = player.getLookAngle();
                net.minecraft.world.phys.Vec3 pos = player.getEyePosition().add(look.scale(1.2));

                level.addParticle(net.minecraft.core.particles.ParticleTypes.GLOW,
                        pos.x, pos.y - 0.2, pos.z,
                        0, 0, 0);
            }
        }
    }
}