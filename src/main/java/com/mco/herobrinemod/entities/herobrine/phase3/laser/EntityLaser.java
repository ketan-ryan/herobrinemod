package com.mco.herobrinemod.entities.herobrine.phase3.laser;

import com.mco.herobrinemod.entities.herobrine.phase3.EntityHardestHerobrine;
import com.mco.herobrinemod.main.HerobrineDamageSources;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class EntityLaser extends Entity
{
    public EntityHardestHerobrine herobrine;
    private Vec3d startVec, endVec;

    private static final DataParameter<Float> YAW = EntityDataManager.createKey(EntityLaser.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> PITCH = EntityDataManager.createKey(EntityLaser.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> HEROBRINE = EntityDataManager.createKey(EntityLaser.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> DURATION = EntityDataManager.createKey(EntityLaser.class, DataSerializers.VARINT);

    private static final double RADIUS = 1.0D;

    public EntityLaser(World world){
        super(world);
        setSize(0.5F, 0.5F);
        ignoreFrustumCheck = true;
    }

    public EntityLaser(World world, EntityHardestHerobrine herobrine, double x, double y, double z, float yaw, float pitch, int duration)
    {
        this(world);
        setYaw(yaw);
        setPitch(pitch);
        this.herobrine = herobrine;
        this.setPosition(x, y, z);
        if(!world.isRemote)
            setHerobrineID(herobrine.getEntityId());
    }

    @Override
    protected void entityInit() {
        getDataManager().register(YAW, 0F);
        getDataManager().register(PITCH, 0F);
        getDataManager().register(DURATION, 0);
        getDataManager().register(HEROBRINE, -1);
    }

    private void laser()
    {
        EntityHardestHerobrine h = (EntityHardestHerobrine)world.getEntityByID(getHerobrineID());
        if(h != null) {
            Vec3d initialVec = startVec = h.getLookVec();
            //Angle the beam
            double x = posX + RADIUS * Math.cos(getYaw() * Math.cos(getPitch()));
            double z = posZ + RADIUS * Math.sin(getYaw() * Math.cos(getPitch()));
            double y = posY + RADIUS * Math.sin(getPitch());
            Vec3d lookFar = new Vec3d(x,y,z);

            if (lookFar != null) {
                BlockPos secondPos = new BlockPos(lookFar);
                //Light a fire at the targeted block
                setFire(secondPos);
                setFire(secondPos.east());
                setFire(secondPos.west());
                setFire(secondPos.north());
                setFire(secondPos.south());

                double diffX = secondPos.getX() - initialVec.x;
                double diffY = secondPos.getY() - initialVec.y;
                double diffZ = secondPos.getZ() - initialVec.z;

                //Get how far away the hit block is from the start
                double length = Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2) + Math.pow(diffZ, 2));
                for (int i = 0; i < length; i++) {
                    //Increase the divisor to increase the frequency of blocks in the line
                    double factorX = diffX * (i / 32.0);
                    double factorY = diffY * (i / 32.0);
                    double factorZ = diffZ * (i / 32.0);

                    Vec3d factorVec = new Vec3d(factorX, factorY, factorZ);
                    Vec3d slopeVec = initialVec.add(factorVec);

                    //Get the current block in the line
                    BlockPos slopePos = new BlockPos(slopeVec);
                    AxisAlignedBB axisPos = new AxisAlignedBB(slopePos.getX(), slopePos.getY(), slopePos.getZ(), slopePos.getX(), slopePos.getY(), slopePos.getZ()).grow(1, 1, 1);
                    List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, axisPos);

                    for (EntityLivingBase entity : entities) {
                        if (entity == herobrine)
                            continue;
                        float pad = entity.getCollisionBorderSize() + 0.5F;
                        AxisAlignedBB alignedBB = entity.getEntityBoundingBox().grow(pad, pad, pad);
                        RayTraceResult rayTraceResult = alignedBB.calculateIntercept(initialVec, slopeVec);

                        if (alignedBB.contains(initialVec) && !world.isRemote) {
                            entity.attackEntityFrom(HerobrineDamageSources.HARD_HEROBRINE, 10);
                        }
                    }
                    //Cosmetic stuff
                    if (ticksExisted % 10 == 0) {
                        world.setBlockToAir(slopePos);
                        world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, slopePos.getX(), slopePos.getY(), slopePos.getZ(),
                                0, 0, 0);
                    }
                }
            }
            endVec = lookFar;
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if(ticksExisted == 1 && world.isRemote)
            herobrine = (EntityHardestHerobrine) world.getEntityByID(getHerobrineID());

        laser();

        if(ticksExisted > 100)
            setDead();
    }

    public void setYaw(float yaw){
        getDataManager().set(YAW, yaw);
    }

    public float getYaw(){
        return getDataManager().get(YAW);
    }

    public void setPitch(float pitch){
        getDataManager().set(PITCH, pitch);
    }

    public float getPitch(){
        return getDataManager().get(PITCH);
    }

    public void setDuration(int duration){
        getDataManager().set(DURATION, duration);
    }

    public int getDuration(){
        return getDataManager().get(DURATION);
    }

    public void setHerobrineID(int id){
        getDataManager().set(HEROBRINE, id);
    }

    public int getHerobrineID(){
        return getDataManager().get(HEROBRINE);
    }

    public Vec3d getStartVec(){
        return startVec;
    }

    public Vec3d getEndVec(){
        return endVec;
    }

    @Override
    public boolean writeToNBTOptional(NBTTagCompound compound) {
        return false;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public boolean isInRangeToRenderDist(double distance) {
        return distance < 2048;
    }

    private void setFire(BlockPos pos)
    {
        if(world.getBlockState(pos).getMaterial() == Material.AIR && Blocks.FIRE.canPlaceBlockAt(world, pos)
                && !world.isRemote)
            world.setBlockState(pos, Blocks.FIRE.getDefaultState());
    }
}
