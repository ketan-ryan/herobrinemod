package com.mco.herobrinemod.armor;

import net.minecraft.core.NonNullList;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class HardArmor extends ArmorItem {
	public HardArmor(ArmorMaterial material, EquipmentSlot slot, Properties properties) {
		super(material, slot, properties);
	}

	@Override
	public void onArmorTick(ItemStack stack, Level level, Player player) {
		super.onArmorTick(stack, level, player);
		if(!player.isSpectator()) {
			NonNullList<ItemStack> armor = player.getInventory().armor;
			int hardPieces, harderPieces, halfHarderPieces, hardestPieces;
			hardPieces = harderPieces = halfHarderPieces = hardestPieces = 0;

			for (ItemStack itemArmor : armor) {
				if (itemArmor != null && itemArmor.getItem() instanceof HardArmor) {
					String noId = itemArmor.getDisplayName().toString().substring(13);

					if(noId.contains("halfharder_"))
						halfHarderPieces++;
					else if(noId.contains("harder_"))
						harderPieces++;
					else if(noId.contains("hardest_"))
						hardestPieces++;
					else if(noId.contains("hard_"))
						hardPieces++;
				}
			}

			if(hardPieces == 4) {
				player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 2, 2));
				player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 2, 1));
				player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 2, 1));
			}
			else if (harderPieces == 4) {
				player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 2, 2));
				player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 2, 1));
				player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 2, 1));
			}
			else if(halfHarderPieces == 4){
				player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 2, 2));
				player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 2, 1));
				player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 2, 3));
				player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 2, 0));
				player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 2, 1));
			}
			else if(hardestPieces == 4){
				player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 2, 3));
				player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 2, 3));
				player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 2, 4));
				player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 2, 1));
				player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 2, 3));
//				player.allowFlying = true;
			}

//			else if(hardestPieces < 4 && !player.isCreative()) {
//				player.capabilities.allowFlying = false;
//				player.capabilities.isFlying = false;
//			}
		}
	}

}
