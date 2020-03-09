package com.mco.herobrinemod.main;

import com.mco.herobrinemod.HerobrineMod;
import com.mco.herobrinemod.armor.HardArmor;
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

    public static Item hard_sword;
    public static Item harder_sword;

    public static ItemArmor.ArmorMaterial hard_material = EnumHelper.addArmorMaterial(HerobrineMod.MODID + ":" + "hard_armor",
            HerobrineMod.MODID + ":" + "hard",40, new int[]{7, 11, 14, 7}, 18, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 8.0F);

    public static ItemArmor.ArmorMaterial harder_material = EnumHelper.addArmorMaterial(HerobrineMod.MODID + ":" + "harder_armor",
            HerobrineMod.MODID + ":" + "harder",60, new int[]{12, 16, 19, 12}, 20, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 11.0F);

    public static Item.ToolMaterial hard_tool = EnumHelper.addToolMaterial("hard", 3, 2342, 12F, 15F, 15);
    public static Item.ToolMaterial harder_tool = EnumHelper.addToolMaterial("harder", 3, 3342, 14F, 21F, 17);

    public static void preInit(){

        hard_sword = new HardSword(hard_tool, "hard_sword", "hard_sword");
        harder_sword = new HardSword(harder_tool, "harder_sword", "harder_sword");

        hard_helmet = new HardArmor(hard_material, EntityEquipmentSlot.HEAD, "hard_helmet", "hard_helmet");
        hard_chestplate = new HardArmor(hard_material, EntityEquipmentSlot.CHEST, "hard_chestplate", "hard_chestplate");
        hard_leggings = new HardArmor(hard_material, EntityEquipmentSlot.LEGS, "hard_leggings", "hard_leggings");
        hard_boots = new HardArmor(hard_material, EntityEquipmentSlot.FEET, "hard_boots", "hard_boots");

        harder_helmet = new HardArmor(harder_material, EntityEquipmentSlot.HEAD, "harder_helmet", "harder_helmet");
        harder_chestplate = new HardArmor(harder_material, EntityEquipmentSlot.CHEST, "harder_chestplate", "harder_chestplate");
        harder_leggings = new HardArmor(harder_material, EntityEquipmentSlot.LEGS, "harder_leggings", "harder_leggings");
        harder_boots = new HardArmor(harder_material, EntityEquipmentSlot.FEET, "harder_boots", "harder_boots");
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
    }

    public static void registerRender(Item item)
    {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(HerobrineMod.MODID + ":" +
                item.getTranslationKey().substring(5), "inventory"));
    }

}
