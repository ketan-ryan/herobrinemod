package com.mco.herobrinemod.armor;

import com.mco.herobrinemod.main.MainItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class HardArmor extends ItemArmor {

    public HardArmor(ArmorMaterial material, EntityEquipmentSlot slot, String registryName, String unlocalizedName){
        super(material, 0, slot);

        setRegistryName(registryName);
        setTranslationKey(unlocalizedName);
        //setCreativeTab()
    }

    public static boolean isWearingFullSet(EntityPlayer player, Item helmet, Item chestplate, Item leggings, Item boots)
    {
        return player.inventory.armorItemInSlot(3) != null && player.inventory.armorItemInSlot(3).getItem() == helmet
                && player.inventory.armorItemInSlot(2) != null && player.inventory.armorItemInSlot(2).getItem() == chestplate
                && player.inventory.armorItemInSlot(1) != null && player.inventory.armorItemInSlot(1).getItem() == leggings
                && player.inventory.armorItemInSlot(0) != null && player.inventory.armorItemInSlot(0).getItem() == boots;
    }

    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack)
    {
        if (isWearingFullSet(player, MainItems.hard_helmet, MainItems.hard_chestplate, MainItems.hard_leggings, MainItems.hard_boots)) {
            player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 2, 0));
            player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 2, 0));
        }
        else if (isWearingFullSet(player, MainItems.harder_helmet, MainItems.harder_chestplate, MainItems.harder_leggings, MainItems.harder_boots)) {
            player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 2, 2));
            player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 2, 1));
            player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 2, 1));
        }
        else if(isWearingFullSet(player, MainItems.halfharder_helmet, MainItems.halfharder_chestplate, MainItems.halfharder_leggings, MainItems.halfharder_boots)){
            player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 2, 2));
            player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 2, 1));
            player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 2, 3));
            player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 2, 0));
            player.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 2, 1));
        }
        else if(isWearingFullSet(player, MainItems.hardest_helmet, MainItems.hardest_chestplate, MainItems.hardest_leggings, MainItems.hardest_boots)){
            player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 2, 3));
            player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 2, 3));
            player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 2, 4));
            player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 2, 1));
            player.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 2, 3));
            player.capabilities.allowFlying = true;
        }
    }
}
