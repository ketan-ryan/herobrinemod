package com.mco.herobrinemod.main;

import com.mco.herobrinemod.HerobrineMod;
import com.mco.herobrinemod.armor.HardArmor;
import com.mco.herobrinemod.armor.HarderArmor;
import com.mco.herobrinemod.items.HardSpawner;
import com.mco.herobrinemod.items.HardSword;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@net.minecraftforge.fml.common.Mod.EventBusSubscriber
@GameRegistry.ObjectHolder(HerobrineMod.MODID)
public class MainItems {

    public static Item hard_helmet;
    public static Item hard_chestplate;
    public static Item hard_leggings;
    public static Item hard_boots;
    
    public static Item harder_helmet;
    public static Item harder_chestplate;
    public static Item harder_leggings;
    public static Item harder_boots;

    public static Item halfharder_helmet;
    public static Item halfharder_chestplate;
    public static Item halfharder_leggings;
    public static Item halfharder_boots;

    public static Item hardest_helmet;
    public static Item hardest_chestplate;
    public static Item hardest_leggings;
    public static Item hardest_boots;
    
    public static Item hard_sword;
    public static Item harder_sword;
    public static Item halfharder_sword;
    public static Item hardest_sword;

    public static Item hard_spawner;

    public static ItemArmor.ArmorMaterial hard_material = EnumHelper.addArmorMaterial(HerobrineMod.MODID + ":" + "hard_armor",
            HerobrineMod.MODID + ":" + "hard",40, new int[]{7, 11, 14, 7}, 18, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 8.0F);

    public static ItemArmor.ArmorMaterial harder_material = EnumHelper.addArmorMaterial(HerobrineMod.MODID + ":" + "harder_armor",
            HerobrineMod.MODID + ":" + "harder",60, new int[]{12, 16, 19, 12}, 20, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 11.0F);

    public static ItemArmor.ArmorMaterial halfharder_material = EnumHelper.addArmorMaterial(HerobrineMod.MODID + ":" + "halfharder_armor",
            HerobrineMod.MODID + ":" + "halfharder",70, new int[]{15, 19, 21, 15}, 21, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 13.0F);
    
    public static ItemArmor.ArmorMaterial hardest_material = EnumHelper.addArmorMaterial(HerobrineMod.MODID + ":" + "hardest_armor",
            HerobrineMod.MODID + ":" + "hardest",80, new int[]{18, 22, 24, 18}, 22, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 16.0F);

    public static Item.ToolMaterial hard_tool = EnumHelper.addToolMaterial("hard", 3, 2342, 12F, 15F, 15);
    public static Item.ToolMaterial harder_tool = EnumHelper.addToolMaterial("harder", 3, 3342, 14F, 21F, 17);
    public static Item.ToolMaterial halfharder_tool = EnumHelper.addToolMaterial("halfharder", 3, 3842, 15F, 24F, 18);
    public static Item.ToolMaterial hardest_tool = EnumHelper.addToolMaterial("hardest", 3, 4342, 18F, 27F, 20);

    public static void preInit(){

        hard_sword = new HardSword(hard_tool, "hard_sword", "hard_sword");
        harder_sword = new HardSword(harder_tool, "harder_sword", "harder_sword");
        halfharder_sword = new HardSword(halfharder_tool, "halfharder_sword", "halfharder_sword");
        hardest_sword = new HardSword(hardest_tool, "hardest_sword", "hardest_sword");
        
        hard_helmet = new HardArmor(hard_material, EntityEquipmentSlot.HEAD, "hard_helmet", "hard_helmet");
        hard_chestplate = new HardArmor(hard_material, EntityEquipmentSlot.CHEST, "hard_chestplate", "hard_chestplate");
        hard_leggings = new HardArmor(hard_material, EntityEquipmentSlot.LEGS, "hard_leggings", "hard_leggings");
        hard_boots = new HardArmor(hard_material, EntityEquipmentSlot.FEET, "hard_boots", "hard_boots");

        harder_helmet = new HardArmor(harder_material, EntityEquipmentSlot.HEAD, "harder_helmet", "harder_helmet");
        harder_chestplate = new HardArmor(harder_material, EntityEquipmentSlot.CHEST, "harder_chestplate", "harder_chestplate");
        harder_leggings = new HardArmor(harder_material, EntityEquipmentSlot.LEGS, "harder_leggings", "harder_leggings");
        harder_boots = new HardArmor(harder_material, EntityEquipmentSlot.FEET, "harder_boots", "harder_boots");

        halfharder_helmet = new HardArmor(halfharder_material, EntityEquipmentSlot.HEAD, "halfharder_helmet", "halfharder_helmet");
        halfharder_chestplate = new HardArmor(halfharder_material, EntityEquipmentSlot.CHEST, "halfharder_chestplate", "halfharder_chestplate");
        halfharder_leggings = new HardArmor(halfharder_material, EntityEquipmentSlot.LEGS, "halfharder_leggings", "halfharder_leggings");
        halfharder_boots = new HardArmor(halfharder_material, EntityEquipmentSlot.FEET, "halfharder_boots", "halfharder_boots");

        hardest_helmet = new HardArmor(hardest_material, EntityEquipmentSlot.HEAD, "hardest_helmet", "hardest_helmet");
        hardest_chestplate = new HardArmor(hardest_material, EntityEquipmentSlot.CHEST, "hardest_chestplate", "hardest_chestplate");
        hardest_leggings = new HardArmor(hardest_material, EntityEquipmentSlot.LEGS, "hardest_leggings", "hardest_leggings");
        hardest_boots = new HardArmor(hardest_material, EntityEquipmentSlot.FEET, "hardest_boots", "hardest_boots");

        hard_spawner = new HardSpawner("hard_spawner");
    }

    /**
     * REGISTER ITEMS ================================================
     */
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().register(hard_helmet);
        event.getRegistry().register(hard_chestplate);
        event.getRegistry().register(hard_leggings);
        event.getRegistry().register(hard_boots);

        event.getRegistry().register(hard_sword);

        event.getRegistry().register(harder_helmet);
        event.getRegistry().register(harder_chestplate);
        event.getRegistry().register(harder_leggings);
        event.getRegistry().register(harder_boots);

        event.getRegistry().register(harder_sword);

        event.getRegistry().register(halfharder_helmet);
        event.getRegistry().register(halfharder_chestplate);
        event.getRegistry().register(halfharder_leggings);
        event.getRegistry().register(halfharder_boots);

        event.getRegistry().register(halfharder_sword);

        event.getRegistry().register(hardest_helmet);
        event.getRegistry().register(hardest_chestplate);
        event.getRegistry().register(hardest_leggings);
        event.getRegistry().register(hardest_boots);

        event.getRegistry().register(hardest_sword);

        event.getRegistry().register(hard_spawner);
    }

    public static void registerRenders()
    {
        registerRender(hard_sword);
        registerRender(hard_leggings);
        registerRender(hard_helmet);
        registerRender(hard_chestplate);
        registerRender(hard_boots);

        registerRender(harder_sword);
        registerRender(harder_leggings);
        registerRender(harder_helmet);
        registerRender(harder_chestplate);
        registerRender(harder_boots);

        registerRender(halfharder_sword);
        registerRender(halfharder_leggings);
        registerRender(halfharder_helmet);
        registerRender(halfharder_chestplate);
        registerRender(halfharder_boots);

        registerRender(hardest_sword);
        registerRender(hardest_leggings);
        registerRender(hardest_helmet);
        registerRender(hardest_chestplate);
        registerRender(hardest_boots);

        registerRender(hard_spawner);
    }

    public static void registerRender(Item item)
    {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(HerobrineMod.MODID + ":" +
                item.getTranslationKey().substring(5), "inventory"));
    }

}
