package net.onequart.qsouls.common.item.weapon;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class GaleHunterItem extends BowItem {
    public GaleHunterItem(Properties properties) {
        super(properties);
    }

    @Override
    public void releaseUsing(ItemStack stack, net.minecraft.world.level.Level level, net.minecraft.world.entity.LivingEntity entity, int timeLeft) {
        super.releaseUsing(stack, level, entity, timeLeft);

        int drawDuration = this.getUseDuration(stack) - timeLeft;
        float power = getPowerForTime(drawDuration);

        if (power > 0.8f && !level.isClientSide && level instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.CLOUD,
                    entity.getX(), entity.getY() + 1.0, entity.getZ(),
                    25, 0.5, 0.5, 0.5, 0.15);
            serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.SWEEP_ATTACK,
                    entity.getX(), entity.getY() + 1.0, entity.getZ(),
                    1, 0.0, 0.0, 0.0, 0.0);
            
            serverLevel.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                    net.minecraft.sounds.SoundEvents.ENDER_DRAGON_FLAP, net.minecraft.sounds.SoundSource.PLAYERS, 0.5f, 2.0f);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("★★★ ").withStyle(ChatFormatting.AQUA)
                .append(Component.translatable("tooltip.qsouls.type.bow")));
        tooltip.add(Component.empty());
        tooltip.add(Component.translatable("item.qsouls.gale_hunter.lore").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        tooltip.add(Component.empty());
        tooltip.add(Component.translatable("tooltip.qsouls.passive").withStyle(ChatFormatting.GOLD)
                .append(Component.translatable("item.qsouls.gale_hunter.passive_name").withStyle(ChatFormatting.WHITE)));
        tooltip.add(Component.translatable("item.qsouls.gale_hunter.passive_desc").withStyle(ChatFormatting.GREEN));
    }

    @Override
    public net.minecraft.network.chat.Component getName(ItemStack stack) {
        return super.getName(stack).copy().withStyle(net.minecraft.ChatFormatting.AQUA);
    }
}