package com.mco.herobrinemod.main;

import com.mco.herobrinemod.HerobrineMod;
import com.mco.herobrinemod.armor.HardArmor;
import com.mco.herobrinemod.items.HardSpawner;
import com.mco.herobrinemod.items.HardSword;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.mco.herobrinemod.main.HerobrineUtils.HerobrineItemTier;

public class HerobrineItems {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, HerobrineMod.MODID);

	public static final RegistryObject<Item> HARD_SPAWNER = ITEMS.register("hard_spawner",
			() -> new HardSpawner(new Item.Properties()));

	public static final RegistryObject<Item> HARD_SWORD = ITEMS.register("hard_sword",
			() -> new HardSword(HerobrineItemTier.HARD, 0, -2.4F, new Item.Properties(),
					"This weapon has been enhanced to combat Herobrine"));

	public static final RegistryObject<Item> HARDER_SWORD = ITEMS.register("harder_sword",
			() -> new HardSword(HerobrineItemTier.HARDER, 0, -2.4F, new Item.Properties(),
					"This weapon has been twice enhanced to combat Herobrine"));

	public static final RegistryObject<Item> HALFHARDER_SWORD = ITEMS.register("halfharder_sword",
			() -> new HardSword(HerobrineItemTier.HALFHARDER, 0, -2.4F, new Item.Properties(),
					"This weapon has been thrice enhanced to combat Herobrine"));

	public static final RegistryObject<Item> HARDEST_SWORD = ITEMS.register("hardest_sword",
			() -> new HardSword(HerobrineItemTier.HARDEST, 0, -2.2F, new Item.Properties(),
					"This is the ultimate weapon for combating Herobrine"));

	public static final RegistryObject<Item> HARD_HELMET = ITEMS.register("hard_helmet",
			() -> new HardArmor(HerobrineUtils.HerobrineArmorMaterial.HARD, EquipmentSlot.HEAD, new Item.Properties()));
	public static final RegistryObject<Item> HARD_CHESTPLATE = ITEMS.register("hard_chestplate",
			() -> new HardArmor(HerobrineUtils.HerobrineArmorMaterial.HARD, EquipmentSlot.CHEST, new Item.Properties()));
	public static final RegistryObject<Item> HARD_LEGGINGS = ITEMS.register("hard_leggings",
			() -> new HardArmor(HerobrineUtils.HerobrineArmorMaterial.HARD, EquipmentSlot.LEGS, new Item.Properties()));
	public static final RegistryObject<Item> HARD_BOOTS = ITEMS.register("hard_boots",
			() -> new HardArmor(HerobrineUtils.HerobrineArmorMaterial.HARD, EquipmentSlot.FEET, new Item.Properties()));

	public static final RegistryObject<Item> HARDER_HELMET = ITEMS.register("harder_helmet",
			() -> new HardArmor(HerobrineUtils.HerobrineArmorMaterial.HARDER, EquipmentSlot.HEAD, new Item.Properties()));
	public static final RegistryObject<Item> HARDER_CHESTPLATE = ITEMS.register("harder_chestplate",
			() -> new HardArmor(HerobrineUtils.HerobrineArmorMaterial.HARDER, EquipmentSlot.CHEST, new Item.Properties()));
	public static final RegistryObject<Item> HARDER_LEGGINGS = ITEMS.register("harder_leggings",
			() -> new HardArmor(HerobrineUtils.HerobrineArmorMaterial.HARDER, EquipmentSlot.LEGS, new Item.Properties()));
	public static final RegistryObject<Item> HARDER_BOOTS = ITEMS.register("harder_boots",
			() -> new HardArmor(HerobrineUtils.HerobrineArmorMaterial.HARDER, EquipmentSlot.FEET, new Item.Properties()));

	public static final RegistryObject<Item> HALFHARDER_HELMET = ITEMS.register("halfharder_helmet",
			() -> new HardArmor(HerobrineUtils.HerobrineArmorMaterial.HALFHARDER, EquipmentSlot.HEAD, new Item.Properties()));
	public static final RegistryObject<Item> HALFHARDER_CHESTPLATE = ITEMS.register("halfharder_chestplate",
			() -> new HardArmor(HerobrineUtils.HerobrineArmorMaterial.HALFHARDER, EquipmentSlot.CHEST, new Item.Properties()));
	public static final RegistryObject<Item> HALFHARDER_LEGGINGS = ITEMS.register("halfharder_leggings",
			() -> new HardArmor(HerobrineUtils.HerobrineArmorMaterial.HALFHARDER, EquipmentSlot.LEGS, new Item.Properties()));
	public static final RegistryObject<Item> HALFHARDER_BOOTS = ITEMS.register("halfharder_boots",
			() -> new HardArmor(HerobrineUtils.HerobrineArmorMaterial.HALFHARDER, EquipmentSlot.FEET, new Item.Properties()));

	public static final RegistryObject<Item> HARDEST_HELMET = ITEMS.register("hardest_helmet",
			() -> new HardArmor(HerobrineUtils.HerobrineArmorMaterial.HARDEST, EquipmentSlot.HEAD, new Item.Properties()));
	public static final RegistryObject<Item> HARDEST_CHESTPLATE = ITEMS.register("hardest_chestplate",
			() -> new HardArmor(HerobrineUtils.HerobrineArmorMaterial.HARDEST, EquipmentSlot.CHEST, new Item.Properties()));
	public static final RegistryObject<Item> HARDEST_LEGGINGS = ITEMS.register("hardest_leggings",
			() -> new HardArmor(HerobrineUtils.HerobrineArmorMaterial.HARDEST, EquipmentSlot.LEGS, new Item.Properties()));
	public static final RegistryObject<Item> HARDEST_BOOTS = ITEMS.register("hardest_boots",
			() -> new HardArmor(HerobrineUtils.HerobrineArmorMaterial.HARDEST, EquipmentSlot.FEET, new Item.Properties()));

	public static void register(IEventBus eventBus) {
		ITEMS.register(eventBus);
	}

	public static class HerobrineCreativeTab extends CreativeModeTab {
		public static final HerobrineCreativeTab instance = new HerobrineCreativeTab(
				CreativeModeTab.builder(Row.BOTTOM, 6).title(Component.translatable("herobrineitems")));
		protected HerobrineCreativeTab(Builder builder) {
			super(builder);
		}

		@Override
		public ItemStack getIconItem() {
			return new ItemStack(HARD_SWORD.get());
		}
	}
}
