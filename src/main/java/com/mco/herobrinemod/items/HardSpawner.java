package com.mco.herobrinemod.items;

import com.google.common.base.Predicate;
import com.mco.herobrinemod.entities.herobrine.phase1.EntityHerobrine;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.pattern.BlockMaterialMatcher;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import static net.minecraft.block.BlockSkull.NODROP;

public class HardSpawner extends Item
{
    private BlockPattern herobrinePattern;

    private static final Predicate<BlockWorldState> IS_WITHER_SKELETON = new Predicate<BlockWorldState>()
    {
        public boolean apply(@Nullable BlockWorldState p_apply_1_)
        {
            return p_apply_1_.getBlockState() != null && p_apply_1_.getBlockState().getBlock() == Blocks.SKULL && p_apply_1_.getTileEntity() instanceof TileEntitySkull && ((TileEntitySkull)p_apply_1_.getTileEntity()).getSkullType() == 1;
        }
    };

    public HardSpawner(String name){
        setRegistryName(name);
        setTranslationKey(name);
        setCreativeTab(CreativeTabs.MISC);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
                                      EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!worldIn.isRemote)
        {
            BlockPattern pattern = getHerobrinePattern();
            BlockPattern.PatternHelper helper = pattern.match(worldIn, pos);
            if(helper != null){
                for (int i = 0; i < 3; ++i)
                {
                    BlockWorldState blockworldstate = helper.translateOffset(i, 0, 0);
                    if(blockworldstate.getBlockState() != Blocks.AIR.getDefaultState())
                        worldIn.setBlockState(blockworldstate.getPos(), blockworldstate.getBlockState().withProperty(NODROP, true), 2);
                }

                for (int j = 0; j < pattern.getPalmLength(); ++j)
                {
                    for (int k = 0; k < pattern.getThumbLength(); ++k)
                    {
                        BlockWorldState blockworldstate1 = helper.translateOffset(j, k, 0);
                        worldIn.setBlockState(blockworldstate1.getPos(), Blocks.AIR.getDefaultState(), 2);
                    }
                }
                BlockPos blockpos = helper.translateOffset(1, 0, 0).getPos();
                EntityHerobrine entityHerobrine = new EntityHerobrine(worldIn);
                BlockPos blockpos1 = helper.translateOffset(1, 2, 0).getPos();
                entityHerobrine.setLocationAndAngles((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.55D,
                        (double)blockpos1.getZ() + 0.5D, helper.getForwards().getAxis() == EnumFacing.Axis.X ? 0.0F : 90.0F, 0.0F);
                entityHerobrine.renderYawOffset = helper.getForwards().getAxis() == EnumFacing.Axis.X ? 0.0F : 90.0F;
                worldIn.spawnEntity(entityHerobrine);

                worldIn.newExplosion(entityHerobrine, blockpos1.getX(), blockpos1.getY(), blockpos1.getZ(), 3, true, true);

                if(!player.isCreative()) {
                    ItemStack itemstack = player.getHeldItem(hand);
                    itemstack.shrink(1);
                }
                for (int l = 0; l < 120; ++l)
                {
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, (double)blockpos.getX() + worldIn.rand.nextDouble(),
                            (double)(blockpos.getY() - 2) + worldIn.rand.nextDouble() * 3.9D,
                            (double)blockpos.getZ() + worldIn.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
                }

                for (int i1 = 0; i1 < pattern.getPalmLength(); ++i1)
                {
                    for (int j1 = 0; j1 < pattern.getThumbLength(); ++j1)
                    {
                        BlockWorldState blockworldstate2 = helper.translateOffset(i1, j1, 0);
                        worldIn.notifyNeighborsRespectDebug(blockworldstate2.getPos(), Blocks.AIR, false);
                    }
                }
            }

        }
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    protected BlockPattern getHerobrinePattern()
    {
        if (this.herobrinePattern == null)
        {
            this.herobrinePattern = FactoryBlockPattern.start().aisle("ASA", "ADA", "AOA").where('D',
                    BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.DIAMOND_BLOCK))).where('O',
                    BlockWorldState.hasState(BlockStateMatcher.forBlock(Blocks.OBSIDIAN))).where('A',
                    BlockWorldState.hasState(BlockMaterialMatcher.forMaterial(Material.AIR))).where('S',
                    IS_WITHER_SKELETON).build();
        }

        return this.herobrinePattern;
    }
}
