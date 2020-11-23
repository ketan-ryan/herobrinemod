package com.mco.herobrinemod.armor;

import com.mco.herobrinemod.main.MainItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class HardArmor extends ItemArmor {

    public HardArmor(ArmorMaterial material, EntityEquipmentSlot slot, String registryName, String unlocalizedName){
        super(material, 0, slot);

        setRegistryName(registryName);
        setTranslationKey(unlocalizedName);
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack)
    {
        if(!player.isSpectator()) {
            NonNullList<ItemStack> armor = player.inventory.armorInventory;
            int hardPieces, harderPieces, halfHarderPieces, hardestPieces;
            hardPieces = harderPieces = halfHarderPieces = hardestPieces = 0;

            for (ItemStack itemArmor : armor) {
                if (itemArmor != null && itemArmor.getItem() instanceof HardArmor) {
                    String noId = itemArmor.getItem().getRegistryName().toString().substring(13);
                    if(noId.contains("hard_"))
                        hardPieces++;
                    else if(noId.contains("harder_"))
                        harderPieces++;
                    else if(noId.contains("halfharder_"))
                        halfHarderPieces++;
                    else if(noId.contains("hardest_"))
                        hardestPieces++;
                }
            }

            if(hardPieces == 4)
            {
                player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 2, 2));
                player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 2, 1));
                player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 2, 1));
            }

            else if (harderPieces == 4) {
                player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 2, 2));
                player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 2, 1));
                player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 2, 1));
            }

            else if(halfHarderPieces == 4){
                player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 2, 2));
                player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 2, 1));
                player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 2, 3));
                player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 2, 0));
                player.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 2, 1));
            }

            else if(hardestPieces == 4){
                player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 2, 3));
                player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 2, 3));
                player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 2, 4));
                player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 2, 1));
                player.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 2, 3));
                player.capabilities.allowFlying = true;
            }

            else if(hardestPieces < 4 && !player.isCreative()) {
                player.capabilities.allowFlying = false;
                player.capabilities.isFlying = false;
            }
        }
    }
}
