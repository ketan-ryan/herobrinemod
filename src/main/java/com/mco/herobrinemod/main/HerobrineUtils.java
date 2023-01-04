package com.mco.herobrinemod.main;

import com.mco.herobrinemod.HerobrineMod;
import com.mco.herobrinemod.entities.herobrine.phase1.Herobrine;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.function.Supplier;

public class HerobrineUtils {
	public enum HerobrineItemTier implements Tier {
		HARD(3, 2342, 1.6F, 18F, 15, () -> {
			return Ingredient.of(Items.DIAMOND);
		}),
		HARDER(3, 3342, 1.6F, 24F, 17, () -> {
			return Ingredient.of(Items.DIAMOND);
		}),
		HALFHARDER(3, 3842, 1.6F, 27F, 18, () -> {
			return Ingredient.of(Items.DIAMOND);
		}),
		HARDEST(3, 4342, 1.6F, 30F, 20, () -> {
			return Ingredient.of(Items.BEDROCK);
		});

		private final int level;
		private final int uses;
		private final float speed;
		private final float damage;
		private final int enchantmentValue;
		private final LazyLoadedValue<Ingredient> repairIngredient;

		HerobrineItemTier(int level, int durability, float miningSpeed, float damage, int enchantability, Supplier<Ingredient> repairIngredient) {
			this.level = level;
			this.uses = durability;
			this.speed = miningSpeed;
			this.damage = damage;
			this.enchantmentValue = enchantability;
			this.repairIngredient = new LazyLoadedValue<>(repairIngredient);
		}

		@Override
		public int getUses() {
			return this.uses;
		}

		@Override
		public float getSpeed() {
			return this.speed;
		}

		@Override
		public float getAttackDamageBonus() {
			return this.damage;
		}

		@Override
		public int getLevel() {
			return this.level;
		}

		@Override
		public int getEnchantmentValue() {
			return this.enchantmentValue;
		}

		@Override
		public Ingredient getRepairIngredient() {
			return this.repairIngredient.get();
		}
	}

	public enum HerobrineArmorMaterial implements ArmorMaterial {
		HARD(HerobrineMod.MODID + ":" + "hard", 40, new int[]{7, 11, 14, 7}, 18, SoundEvents.ARMOR_EQUIP_DIAMOND, 8.0F, 0.1F, () -> {
			return Ingredient.of(Items.DIAMOND);
		}),
		HARDER(HerobrineMod.MODID + ":" + "harder", 0, new int[]{12, 16, 19, 12}, 18, SoundEvents.ARMOR_EQUIP_DIAMOND, 11.0F, 0.1F, () -> {
			return Ingredient.of(Items.DIAMOND);
		}),
		HALFHARDER(HerobrineMod.MODID + ":" + "halfharder", 70, new int[]{15, 19, 21, 15}, 21, SoundEvents.ARMOR_EQUIP_DIAMOND, 13.0F, 0.1F, () -> {
			return Ingredient.of(Items.DIAMOND);
		}),
		HARDEST(HerobrineMod.MODID + ":" + "hardest", 80, new int[]{18, 22, 24, 18}, 22, SoundEvents.ARMOR_EQUIP_DIAMOND, 16.0F, 0.25F, () -> {
			return Ingredient.of(Items.DIAMOND);
		});
		private static final int[] HEALTH_PER_SLOT = new int[]{13, 15, 16, 11};
		private final String name;
		private final int durabilityMultiplier;
		private final int[] slotProtections;
		private final int enchantmentValue;
		private final SoundEvent sound;
		private final float toughness;
		private final float knockbackResistance;
		private final LazyLoadedValue<Ingredient> repairIngredient;

		HerobrineArmorMaterial(String name, int durability, int[] protection, int enchantability, SoundEvent sound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredient) {
			this.name = name;
			this.durabilityMultiplier = durability;
			this.slotProtections = protection;
			this.enchantmentValue = enchantability;
			this.sound = sound;
			this.toughness = toughness;
			this.knockbackResistance = knockbackResistance;
			this.repairIngredient = new LazyLoadedValue<>(repairIngredient);
		}

		public int getDurabilityForSlot(EquipmentSlot slot) {
			return HEALTH_PER_SLOT[slot.getIndex()] * this.durabilityMultiplier;
		}

		public int getDefenseForSlot(EquipmentSlot slot) {
			return this.slotProtections[slot.getIndex()];
		}

		public int getEnchantmentValue() {
			return this.enchantmentValue;
		}

		public SoundEvent getEquipSound() {
			return this.sound;
		}

		public Ingredient getRepairIngredient() {
			return this.repairIngredient.get();
		}

		public String getName() {
			return this.name;
		}

		public float getToughness() {
			return this.toughness;
		}

		public float getKnockbackResistance() {
			return this.knockbackResistance;
		}
	}

	public static final List<MemoryModuleType<?>> BASE_MEMORY_TYPES = List.of(
			MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
			MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER,
			MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.LOOK_TARGET, MemoryModuleType.WALK_TARGET,
			MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.ATTACK_TARGET,
			MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.NEAREST_ATTACKABLE,
			MemoryModuleType.RECENT_PROJECTILE, HerobrineMemoryModules.ATTACK_DELAY.get(),
			HerobrineMemoryModules.FIREBALL_SHOOT_COOLDOWN.get(), HerobrineMemoryModules.FIREBALL_SHOOT_INTERVAL.get(),
			HerobrineMemoryModules.FIREBALL_SHOOT_DELAY.get()
	);

	public static final List<MemoryModuleType<?>> HARD_MEMORY_TYPES = List.of(
			HerobrineMemoryModules.BREATHE_FIRE_COOLDOWN.get(), HerobrineMemoryModules.BREATHE_FIRE_DELAY.get()
	);

	public static final List<SensorType<? extends Sensor<? super Herobrine>>> HEROBRINE_SENSOR_TYPES = ImmutableList.of(
			SensorType.NEAREST_PLAYERS, SensorType.NEAREST_LIVING_ENTITIES, SensorType.HURT_BY,
			HerobrineSensors.HEROBRINE_ENTITY_SENSOR.get());

	public static final float SPEED_MULTIPLIER_WHEN_FIGHTING = 1.75F;
}
