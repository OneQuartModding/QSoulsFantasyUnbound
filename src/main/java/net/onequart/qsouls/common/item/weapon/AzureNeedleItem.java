package net.onequart.qsouls.common.item.weapon;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class AzureNeedleItem extends SwordItem {
    public AzureNeedleItem(Properties properties) {
        super(Tiers.IRON, 3, -2.4f, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, net.minecraft.world.entity.LivingEntity target, net.minecraft.world.entity.LivingEntity attacker) {
        attacker.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.MOVEMENT_SPEED, 40, 1));

        if (!attacker.level().isClientSide && attacker.level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            for (int i = 0; i < 360; i += 30) {
                double xOffset = Math.cos(Math.toRadians(i)) * 1.5;
                double zOffset = Math.sin(Math.toRadians(i)) * 1.5;
                serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.SOUL_FIRE_FLAME,
                        target.getX() + xOffset, target.getY() + target.getBbHeight() / 2.0, target.getZ() + zOffset,
                        1, 0, 0, 0, 0.02);
            }

            serverLevel.playSound(null, target.getX(), target.getY(), target.getZ(),
                    net.minecraft.sounds.SoundEvents.PLAYER_ATTACK_SWEEP, net.minecraft.sounds.SoundSource.PLAYERS, 1.0f, 1.5f);
        }

        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("★★★ ").withStyle(ChatFormatting.AQUA)
                .append(Component.translatable("tooltip.qsouls.type.sword")));
        tooltip.add(Component.empty());
        tooltip.add(Component.translatable("item.qsouls.azure_needle.lore").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        tooltip.add(Component.empty());
        tooltip.add(Component.translatable("tooltip.qsouls.passive").withStyle(ChatFormatting.GOLD)
                .append(Component.translatable("item.qsouls.azure_needle.passive_name").withStyle(ChatFormatting.WHITE)));
        tooltip.add(Component.translatable("item.qsouls.azure_needle.passive_desc").withStyle(ChatFormatting.GREEN));
    }

    @Override
    public net.minecraft.network.chat.Component getName(ItemStack stack) {
        return super.getName(stack).copy().withStyle(net.minecraft.ChatFormatting.AQUA);
    }
}