package com.mco.herobrinemod.entities.herobrine.phase3;

import com.mco.herobrinemod.config.HerobrineConfig;
import com.mco.herobrinemod.entities.herobrine.phase3.ai.AILaser;
import com.mco.herobrinemod.main.HerobrineDamageSources;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationAI;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EntityHardestHerobrine extends EntityMob implements IAnimatedEntity
{
    private final BossInfoServer bossInfo = (BossInfoServer)(new BossInfoServer(this.getDisplayName(), BossInfo.Color.WHITE,
            BossInfo.Overlay.PROGRESS)).setDarkenSky(true);

    private Vec3d startPos;
    private Vec3d endPos;

    private Animation animation = NO_ANIMATION;
    private int animationTick;

    Animation ANIMATION_LASER = Animation.create(91);
    private static Animation ANIMATION_DEATH = Animation.create(200);

    private final Animation[] ANIMATIONS = {ANIMATION_LASER, ANIMATION_DEATH};

    public AnimationAI currentAnim;

    public EntityHardestHerobrine(World world){
        super(world);
        setSize(15, 60);
        ignoreFrustumCheck = true;
        isImmuneToFire = true;
        tasks.addTask(1, new AILaser(this, ANIMATION_LASER));
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(66.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.33000000417232513D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(6.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(666);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        if(getAnimation() != NO_ANIMATION)
        {
            animationTick++;
            if(world.isRemote && animationTick >= animation.getDuration())
                setAnimation(NO_ANIMATION);
        }

        if(currentAnim == null && getAnimation() == NO_ANIMATION && getAnimation() != ANIMATION_DEATH)
        {
            switch(new Random().nextInt(32))
            {
                case 0:
                    AnimationHandler.INSTANCE.sendAnimationMessage(this, ANIMATION_LASER);
                    System.out.println("laser");
                    break;

                default:
                    break;
            }
        }

        if(getAnimation() == ANIMATION_LASER)
        {
            this.rotationPitch = 38;

            if(getAnimationTick() > 1 && getAnimationTick() < 90)
            {
                setRotationYawHead(prevRotationYawHead + 1F);
                laser();
            }
            else
                setRotationYawHead(prevRotationYawHead - 17F);
        }

        updateAITasks();
    }

    /**
     * Performs all the logic behind the laser
     * Generates a vector from the eyes to the target block
     * Sets blocks along the vector to air
     * Spawns particles along the beam
     * Attacks and burns any entities in the path
     * If ocnfig enabled, sets fire to the ground
     */
    private void laser()
    {
        Vec3d initialVec = startPos = this.getPositionEyes(1);

        Vec3d eyeVec = this.getPositionEyes(1).add(0, -10, 0);
        Vec3d lookVec = this.getLook(1);
        Vec3d scaleEye = eyeVec.add(lookVec.x * 75, lookVec.y * 75, lookVec.z * 75);

        //Get the block or entity within 100 blocks of the start vec
        RayTraceResult rayTrace = world.rayTraceBlocks(eyeVec, scaleEye, true, false, true);
        Vec3d lookFar = rayTrace.hitVec;
        if(lookFar != null)
        {
            BlockPos secondPos = new BlockPos(lookFar);
            BlockPos cornerPos = secondPos;

            double diffX = secondPos.getX() - initialVec.x;
            double diffY = secondPos.getY() - initialVec.y;
            double diffZ = secondPos.getZ() - initialVec.z;

            //Get how far away the hit block is from the start
            double length = Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2) + Math.pow(diffZ, 2));
            for (int i = 0; i < length; i++)
            {
                //Increase the divisor to increase the frequency of blocks in the line
                double factorX = diffX * (i / 32.0);
                double factorY = diffY * (i / 32.0);
                double factorZ = diffZ * (i / 32.0);
                Vec3d factorVec = new Vec3d(factorX, factorY, factorZ);
                Vec3d slopeVec = initialVec.add(factorVec);

                //Get the current block in the line
                BlockPos slopePos = new BlockPos(slopeVec);

                //Attack anyone in range of the beam
                AxisAlignedBB box = new AxisAlignedBB(slopePos.getX() + -2, slopePos.getY() + -2,
                        slopePos.getZ() + -2, slopePos.getX() + 2, slopePos.getY() + 2, slopePos.getZ() + 2);

                for(Entity e : world.getEntitiesWithinAABB(Entity.class, box)) {
                    e.attackEntityFrom(HerobrineDamageSources.HARD_LASER, HerobrineConfig.laserDamage);
                    e.setFire(10);
                }

                //Creates a cube of air along the beam
                for(double boxX = box.minX; boxX < box.maxX; boxX++)
                {
                    for(double boxY = box.minY; boxY < box.maxY; boxY++)
                    {
                        for(double boxZ = box.minZ; boxZ < box.maxZ; boxZ++)
                        {
                            BlockPos axisPos = new BlockPos(boxX, boxY, boxZ);
                            if(!world.isAirBlock(axisPos) && world.getBlockState(axisPos).getMaterial() != Material.WATER
                            && world.getBlockState(axisPos).getMaterial() != Material.LAVA && world.getBlockState(axisPos) != Blocks.BEDROCK.getDefaultState()
                            && world.getBlockState(axisPos) != Blocks.FIRE.getDefaultState())
                                world.setBlockToAir(axisPos);
                        }
                    }
                }

                //Spawns flame and end rod particles along the beam to convey heat
                if(rand.nextInt(5) == 0)
                world.spawnParticle(EnumParticleTypes.END_ROD, slopePos.getX() + (this.rand.nextDouble() - 0.5D) * 4.0D,
                        slopePos.getY() + this.rand.nextDouble() * 6.0D, slopePos.getZ() + (this.rand.nextDouble() - 0.5D) * 4.0D, 0, 0, 0);

                if(rand.nextInt(2) == 0)
                world.spawnParticle(EnumParticleTypes.FLAME, slopePos.getX() + (this.rand.nextDouble() - 0.5D) * 4.0D,
                        slopePos.getY() + this.rand.nextDouble() * 6.0D, slopePos.getZ() + (this.rand.nextDouble() - 0.5D) * 4.0D, 0, 0, 0);
            }

            //Checks if the block is valid for placing fire
            //and if setting fire is enabled in the config
            for(int x = 0; x < 5; x++)
            {
                for(int z = 0; z < 5; z++)
                {
                    BlockPos pos = new BlockPos(x + cornerPos.getX(), cornerPos.getY(), z + cornerPos.getZ());
                    if(!world.isRemote && HerobrineConfig.laserFire)
                    {
                        BlockPos airPos = checkAir(pos);
                        if(Blocks.FIRE.canPlaceBlockAt(world, airPos))
                            world.setBlockState(airPos, Blocks.FIRE.getDefaultState());
                    }
                }
            }
        }
        this.endPos = lookFar;
    }

    /**
     * Checks a given pos to see where around it is air
     * @param pos the pos to check at
     * @return an air block in the same x and z coordinates
     */
    private BlockPos checkAir(BlockPos pos)
    {
        if(pos.getY() < 256 && pos.getY() > 0) {
            if (world.getBlockState(pos).getMaterial() == Material.AIR) {
                if (world.getBlockState(pos.down()).getMaterial() != Material.AIR)
                    return pos;
                else if (world.getBlockState(pos.down()).getMaterial() == Material.AIR)
                    checkAir(pos.down());
            } else if (world.canBlockSeeSky(pos))
                checkAir(pos.up());
        }
        return pos;
    }

    /**
     * Gets the starting position of the laser
     * @return the starting position of the laser
     */
    Vec3d getStartPos(){
        return startPos;
    }

    /**
     * Returns the end position of the laser
     * @return the end position of the laser
     */
    Vec3d getEndPos() {
        return endPos;
    }

    private void createShockwave(BlockPos initialPos, float size)
    {
        List<BlockPos> blocks = new ArrayList<BlockPos>();
        blocks.add(initialPos);

        double inX = initialPos.getX();
        double inY = initialPos.getY();
        double inZ = initialPos.getZ();

        for(int i = 0; i < size * 1.5; i++)
        {
            for(float f = 0; f < 360; f+=15)
            {
                double x = 2 * Math.sin(Math.toRadians(f));
                double z = 2 * Math.cos(Math.toRadians(f));

                BlockPos tempPos = new BlockPos(x + inX, inY, z + inZ);
                BlockPos shockPos = checkAir(tempPos, 5);

                EntityFallingBlock entityFallingBlock = new EntityFallingBlock(world, shockPos.getX(), shockPos.getY(),
                        shockPos.getZ(), world.getBlockState(shockPos));
                entityFallingBlock.motionY = 1;
                EntityPig pig = new EntityPig(world);
                pig.setPosition(shockPos.getX(), shockPos.getY(), shockPos.getZ());
                if(!world.isRemote)
                    world.spawnEntity(entityFallingBlock);
                //world.spawnEntity(entityFallingBlock);
            }
        }
    }

    /**
     * Checks a block for air with a given depth to stop checking at
     * @param pos the x and z coords of the block to check at
     * @param depth how far we should look for an air block, caps at 5
     * @return the closest air block
     */
    private BlockPos checkAir(BlockPos pos, int depth)
    {
        if(world.isAirBlock(pos) && depth < 5)
        {
            BlockPos downPos = pos.down();
            checkAir(downPos, depth + 1);
        }
        else if(depth < 5)
        {
            if(world.isAirBlock(pos.up()))
                return pos;
            else{
                BlockPos upPos = pos.up();
                checkAir(upPos, depth + 1);
            }
        }
        return pos;
    }

    /**
     * How bright to render the entity
     * @return a fixed brightness value
     */
    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender()
    {
        return 15728880;
    }

    /**
     * Updates bossbar
     */
    protected void updateAITasks() {
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
    }

    /**
     * Sets the custom name tag for this entity
     */
    public void setCustomNameTag(String name)
    {
        super.setCustomNameTag(name);
        this.bossInfo.setName(this.getDisplayName());
    }

    /**
     * Add the given player to the list of players tracking this entity. For instance, a player may track a boss in
     * order to view its associated boss bar.
     */
    public void addTrackingPlayer(EntityPlayerMP player)
    {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    /**
     * Removes the given player from the list of players tracking this entity. See {@link net.minecraft.entity.Entity#addTrackingPlayer} for
     * more information on tracking.
     */
    public void removeTrackingPlayer(EntityPlayerMP player)
    {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    /**
     * Whether you can pass through the entity
     * Always returns true so you can't walk through it
     * @return whether the entity can be collided with
     */
    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    /**
     * Overrides so not slowed by cobwebs
     */
    @Override
    public void setInWeb() {
    }

    /**
     * Overriden to negate fall damage
     * @param distance how far the entity falls
     * @param damageMultiplier the damage multiplier for falling
     */
    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    /**
     * Since it's so big, we have to override these
     * to make it not disappear when you look up
     * @param x the x amount
     * @param y the y amount
     * @param z the z amount
     */
    @SideOnly(Side.CLIENT)
    @Override
    public boolean isInRangeToRender3d(double x, double y, double z)
    {
        double d0 = this.posX - x;
        double d1 = this.posY + y;
        double d2 = this.posZ - z;
        double d3 = d0 * d0 + d1 * d1 + d2 * d2;
        return this.isInRangeToRenderDist(d3);
    }

    /**
     * Whether the entity is in range to be rendered
     * @param distance the distance from the entity
     * @return whether the entity is in range
     */
    @SideOnly(Side.CLIENT)
    @Override
    public boolean isInRangeToRenderDist(double distance)
    {
        double d0 = this.getEntityBoundingBox().getAverageEdgeLength();

        if (Double.isNaN(d0))
        {
            d0 = 1.0D;
        }

        d0 = d0 * 128.0D;
        return distance < d0 * d0;
    }

    @Override
    public boolean isNonBoss() {
        return false;
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    public Animation getAnimation() {
        return this.animation;
    }

    @Override
    public int getAnimationTick() {
        return this.animationTick;
    }

    @Override
    public void setAnimationTick(int animationTick) {
        this.animationTick = animationTick;
    }

    @Override
    public void setAnimation(Animation animation) {
        if(animation == NO_ANIMATION){
            setAnimationTick(0);
        }
        this.animation = animation;
    }

    @Override
    public Animation[] getAnimations() {
        return ANIMATIONS;
    }

    public static Animation getDeathAnimation() {
        return ANIMATION_DEATH;
    }

}
