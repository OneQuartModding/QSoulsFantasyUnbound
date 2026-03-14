package net.onequart.qsouls.common.item.weapon;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class TitanRibItem extends SwordItem {
    private final Multimap<Attribute, AttributeModifier> customAttributes;

    public TitanRibItem(Properties properties) {
        super(Tiers.IRON, 7, -3.2f, properties);

        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(super.getDefaultAttributeModifiers(EquipmentSlot.MAINHAND));
        builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(UUID.fromString("61796123-0102-4c2c-98d0-c5332c028881"), "Weapon modifier", 0.5D, AttributeModifier.Operation.ADDITION));
        this.customAttributes = builder.build();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? this.customAttributes : super.getDefaultAttributeModifiers(slot);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("★★★ ").withStyle(ChatFormatting.AQUA)
                .append(Component.translatable("tooltip.qsouls.type.claymore")));
        tooltip.add(Component.empty());
        tooltip.add(Component.translatable("item.qsouls.titan_rib.lore").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        tooltip.add(Component.empty());
        tooltip.add(Component.translatable("tooltip.qsouls.passive").withStyle(ChatFormatting.GOLD)
                .append(Component.translatable("item.qsouls.titan_rib.passive_name").withStyle(ChatFormatting.WHITE)));
        tooltip.add(Component.translatable("item.qsouls.titan_rib.passive_desc").withStyle(ChatFormatting.GREEN));
    }

    @Override
    public net.minecraft.network.chat.Component getName(ItemStack stack) {
        return super.getName(stack).copy().withStyle(net.minecraft.ChatFormatting.AQUA);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, net.minecraft.world.entity.LivingEntity target, net.minecraft.world.entity.LivingEntity attacker) {
        if (!attacker.level().isClientSide && attacker.level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.LARGE_SMOKE,
                    target.getX(), target.getY() + 0.5, target.getZ(),
                    20, 0.5, 0.5, 0.5, 0.05);
            serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    target.getX(), target.getY() + 1.0, target.getZ(),
                    15, 0.5, 0.5, 0.5, 0.1);

            serverLevel.playSound(null, target.getX(), target.getY(), target.getZ(),
                    net.minecraft.sounds.SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR, net.minecraft.sounds.SoundSource.PLAYERS, 0.5f, 0.8f);

            target.setDeltaMovement(target.getDeltaMovement().add(0, 0.3, 0));
            target.hurtMarked = true;
        }

        return super.hurtEnemy(stack, target, attacker);
    }
}