package com.mco.herobrinemod.items;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class HardSpawner extends Item {
	public HardSpawner(Properties properties) {
		super(properties);
	}

	@Nullable
	private BlockPattern herobrineBase;
	@Nullable
	private BlockPattern herobrineFull;

	private static final Predicate<BlockState> PUMPKINS_PREDICATE =
			(predicate) -> predicate != null && (predicate.is(Blocks.CARVED_PUMPKIN) || predicate.is(Blocks.JACK_O_LANTERN));

	@Override
	public InteractionResult useOn(UseOnContext context) {
		this.trySpawnHerobrine(context.getLevel(), context.getClickedPos());
		return super.useOn(context);
	}

	private void trySpawnHerobrine(Level level, BlockPos pos) {
		BlockPattern.BlockPatternMatch blockpattern$blockpatternmatch = this.getOrCreateHerobrineFull().find(level, pos);
		if (blockpattern$blockpatternmatch != null) {
			SnowGolem snowgolem = EntityType.SNOW_GOLEM.create(level);
			if (snowgolem != null) {
				spawnHerobrineInWorld(level, blockpattern$blockpatternmatch, snowgolem, blockpattern$blockpatternmatch.getBlock(0, 2, 0).getPos());
			}
		}
	}

	private static void spawnHerobrineInWorld(Level level, BlockPattern.BlockPatternMatch blockPattern, Entity entity, BlockPos pos) {
		clearPatternBlocks(level, blockPattern);
		entity.moveTo((double)pos.getX() + 0.5D, (double)pos.getY() + 0.05D, (double)pos.getZ() + 0.5D, 0.0F, 0.0F);
		level.addFreshEntity(entity);

		for(ServerPlayer serverplayer : level.getEntitiesOfClass(ServerPlayer.class, entity.getBoundingBox().inflate(5.0D))) {
			CriteriaTriggers.SUMMONED_ENTITY.trigger(serverplayer, entity);
		}

		updatePatternBlocks(level, blockPattern);
	}

	public static void clearPatternBlocks(Level level, BlockPattern.BlockPatternMatch blockPattern) {
		for(int i = 0; i < blockPattern.getWidth(); ++i) {
			for(int j = 0; j < blockPattern.getHeight(); ++j) {
				BlockInWorld blockinworld = blockPattern.getBlock(i, j, 0);
				level.setBlock(blockinworld.getPos(), Blocks.AIR.defaultBlockState(), 2);
				level.levelEvent(2001, blockinworld.getPos(), Block.getId(blockinworld.getState()));
			}
		}
	}

	public static void updatePatternBlocks(Level level, BlockPattern.BlockPatternMatch blockPattern) {
		for(int i = 0; i < blockPattern.getWidth(); ++i) {
			for(int j = 0; j < blockPattern.getHeight(); ++j) {
				BlockInWorld blockinworld = blockPattern.getBlock(i, j, 0);
				level.blockUpdated(blockinworld.getPos(), Blocks.AIR);
			}
		}
	}

	private BlockPattern getOrCreateHerobrineBase() {
		if (this.herobrineBase == null) {
			this.herobrineBase = BlockPatternBuilder.start().aisle(" ", "#", "O")
					.where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.DIAMOND_BLOCK)))
					.where('O', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.OBSIDIAN))).build();
		}
		return this.herobrineBase;
	}

	private BlockPattern getOrCreateHerobrineFull() {
		if (this.herobrineFull == null) {
			this.herobrineFull = BlockPatternBuilder.start().aisle("W", "#", "O")
					.where('W', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.WITHER_SKELETON_SKULL).or(BlockStatePredicate.forBlock(Blocks.WITHER_SKELETON_WALL_SKULL))))
					.where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.DIAMOND_BLOCK)))
					.where('O', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.OBSIDIAN))).build();
		}
		return this.herobrineFull;
	}
}
