package com.mco.herobrinemod.entities.herobrine.phase3;

import net.minecraft.client.particle.ParticleRedstone;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityHardestHerobrine extends EntityMob
{
    public EntityHardestHerobrine(World world){
        super(world);
        setSize(1, 2);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        rotationPitch = 45;

        laser();
        System.out.println(getLookVec());
    }

    private void laser() {
        if (!world.isRemote) {
            Vec3d initialVec = this.getPositionEyes(1);
            BlockPos secondPos = new BlockPos(getLookVec());

            world.setBlockState(secondPos, Blocks.FIRE.getDefaultState());

            double diffX = secondPos.getX() - initialVec.x;
            double diffY = secondPos.getY() - initialVec.y;
            double diffZ = secondPos.getZ() - initialVec.z;

            int length = (int) Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2) + Math.pow(diffZ, 2));

            for (int i = 0; i < length; i++) {
                double factorX = diffX * i;
                double factorY = diffY * i;
                double factorZ = diffZ * i;

                Vec3d factorVec = new Vec3d(factorX, factorY, factorZ);
                Vec3d slopeVec = initialVec.add(factorVec);

                BlockPos slopePos = new BlockPos(slopeVec);

                world.spawnParticle(EnumParticleTypes.REDSTONE, slopePos.getX(), slopePos.getY(), slopePos.getZ(),
                        0,0,0);

                if (!world.isAirBlock(slopePos))
                    world.setBlockToAir(slopePos);
            }
        }
    }
}
