package com.mco.herobrinemod.entities.herobrine.phase3;

import net.minecraft.block.material.Material;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityHardestHerobrine extends EntityMob
{
    public EntityHardestHerobrine(World world){
        super(world);
        setSize(15, 60);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        rotationPitch = 37;

        laser();
    }

    private void laser()
    {
        Vec3d initialVec = this.getPositionEyes(1);
        RayTraceResult rayTrace = this.rayTrace(200,1);
        Vec3d lookFar = rayTrace.hitVec;
        BlockPos secondPos = new BlockPos(lookFar);

        if (world.getBlockState(secondPos).getMaterial() == Material.AIR &&
                Blocks.FIRE.canPlaceBlockAt(world, secondPos) && !world.isRemote)
            world.setBlockState(secondPos, Blocks.FIRE.getDefaultState());

        double diffX = secondPos.getX() - initialVec.x;
        double diffY = secondPos.getY() - initialVec.y;
        double diffZ = secondPos.getZ() - initialVec.z;

        double length = Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2) + Math.pow(diffZ, 2));
        for (int i = 0; i < length; i++)
        {
            double factorX = diffX * (i/32.0);
            double factorY = diffY * (i/32.0);
            double factorZ = diffZ * (i/32.0);

            Vec3d factorVec = new Vec3d(factorX, factorY, factorZ);
            Vec3d slopeVec = initialVec.add(factorVec);

            BlockPos slopePos = new BlockPos(slopeVec);
            if(ticksExisted % 10 == 0)
                world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, slopePos.getX(), slopePos.getY(), slopePos.getZ(),
                    0,0,0);

        }
    }

    @Override
    public boolean isNonBoss() {
        return false;
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }
}
