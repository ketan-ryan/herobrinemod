package com.mco.herobrinemod.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HardSword extends SwordItem {
	private String tooltip;

	public HardSword(Tier tier, int damage, float attackSpeed, Properties properties, String tooltip) {
		super(tier, damage, attackSpeed, properties);
		this.tooltip = tooltip;
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
//		if(target != null && attacker != null
//				&& (target instanceof EntityHerobrine || target instanceof EntityHardHerobrine)) {
//			target.setHealth(target.getHealth() - this.getDamage());
//		}
		return super.hurtEnemy(stack, target, attacker);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, level, tooltip, flag);
		tooltip.add(Component.literal(this.tooltip).withStyle(ChatFormatting.DARK_PURPLE));
	}
}
