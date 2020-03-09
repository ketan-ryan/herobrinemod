package com.mco.herobrinemod.items;

import com.mco.herobrinemod.entities.herobrine.phase1.EntityHerobrine;
import com.mco.herobrinemod.entities.herobrine.phase2.EntityHardHerobrine;
import com.mco.herobrinemod.main.MainItems;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class HardSword extends ItemSword {

    private ToolMaterial material;

    public HardSword(ToolMaterial material, String registryName, String unlocalizedName){
        super(material);
        this.material = material;
        setTranslationKey(unlocalizedName);
        setRegistryName(registryName);

      //  this.setCreativeTab();
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        if(target != null && attacker != null && stack.getDisplayName().equals("Fully Enhanced Sword")){
            if(target instanceof EntityHerobrine || target instanceof EntityHardHerobrine){
                target.setHealth(target.getHealth() - material.getAttackDamage());
            }
        }
        if(target != null && attacker != null && stack.getDisplayName().equals("Thrice Enhanced Sword")){
            if(target instanceof EntityHerobrine || target instanceof EntityHardHerobrine){
                target.setHealth(target.getHealth() - material.getAttackDamage());
            }
        }
        if(target != null && attacker != null && stack.getDisplayName().equals("Twice Enhanced Sword")){
            if(target instanceof EntityHerobrine || target instanceof EntityHardHerobrine){
                  target.setHealth(target.getHealth() - material.getAttackDamage());
            }
        }
        if(target != null && attacker != null && stack.getDisplayName().equals("Enhanced Sword")){
            if(target instanceof EntityHerobrine || target instanceof EntityHardHerobrine){
                target.setHealth(target.getHealth() - material.getAttackDamage());
            }
        }
        return super.hitEntity(stack, target, attacker);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        if(stack.getItem().equals(MainItems.hard_sword))
            tooltip.add(TextFormatting.DARK_PURPLE + "This weapon has been enhanced to combat Herobrine");

        else if(stack.getItem().equals(MainItems.harder_sword))
            tooltip.add(TextFormatting.DARK_PURPLE + "This weapon has been twice enhanced to combat Herobrine");

        else if(stack.getItem().equals(MainItems.halfharder_sword))
            tooltip.add(TextFormatting.DARK_PURPLE + "This weapon has been thrice enhanced to combat Herobrine");

        else if(stack.getItem().equals(MainItems.hardest_sword))
            tooltip.add(TextFormatting.DARK_PURPLE + "This is the ultimate weapon for combating Herobrine");
    }
}
