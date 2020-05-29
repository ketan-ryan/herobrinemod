package com.mco.herobrinemod.entities.util;

import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ParticleHelper
{
    /**
     * Creates a cube of particles around a position with a given radius
     * @param world The world object
     * @param types The particle to spawn
     * @param radius The radius of the cube
     * @param yOffset The height of the cube/rectangular prism
     * @param stepSize How dense the cube should be
     * @param pos The position at which to center the cube
     * @param xSpeed The particle's speed in the x direction
     * @param ySpeed The particle's speed in the y direction
     * @param zSpeed The particle's speed in the z direction
    **/
    public static void createParticleCube (World world, EnumParticleTypes types, int radius, int yOffset, float stepSize, Vec3d pos,
                                           double xSpeed, double ySpeed, double zSpeed) {
        //Creates a top and bottom corner and fills in the cube
        BlockPos topCorner = new BlockPos(pos.x + radius, pos.y + yOffset, pos.z + radius);
        BlockPos bottomCorner = new BlockPos(pos.x - radius, pos.y, pos.z - radius);

        for(int i = 0; i <= stepSize; i++)
        {
            double x = ((topCorner.getX() - bottomCorner.getX()) / stepSize) * i;
            for(int f = 0; f <= stepSize; f++)
            {
                double y = ((topCorner.getY() - bottomCorner.getY()) / stepSize) * f;
                for(int g = 0; g <= stepSize; g++)
                {
                    //Add the bottom corner's position to spawn them at the entity's relative location instead of absolute coords
                    double z = ((topCorner.getZ() - bottomCorner.getZ()) / stepSize) * g;
                    world.spawnParticle(types, x + bottomCorner.getX(), y + bottomCorner.getY(),
                            z + bottomCorner.getZ(), xSpeed, ySpeed, zSpeed);
                }
            }
        }
    }
}
