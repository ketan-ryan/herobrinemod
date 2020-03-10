package com.mco.herobrinemod.entities.herobrine.phase1;

import com.google.common.base.Predicate;
import com.mco.herobrinemod.entities.herobrine.phase2.EntityHardHerobrine;
import com.mco.herobrinemod.entities.herobrine.phase2.ghast.EntityCorruptedGhast;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationAI;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class EntityHerobrine extends EntityMob implements IRangedAttackMob, IMob, IAnimatedEntity {

    private final BossInfoServer bossInfo = (BossInfoServer)(new BossInfoServer(this.getDisplayName(), BossInfo.Color.WHITE,
            BossInfo.Overlay.PROGRESS)).setDarkenSky(true);

    private static final DataParameter<Boolean> ATTACKING = EntityDataManager.<Boolean>createKey(EntityHerobrine.class, DataSerializers.BOOLEAN);

    private Animation animation = NO_ANIMATION;
    private int animationTick;

    public static final Animation ANIMATION_DEATH = Animation.create(200);

    private static final Animation[] ANIMATIONS = {ANIMATION_DEATH};

    public AnimationAI currentAnim;

    public int deathTicks;
    //If this entity is being spawned from a phase 2 herobrine
    private boolean secondSpawn;

    /** Selector used to determine the entities a wither boss should attack. */
    private static final Predicate<Entity> NOT_UNDEAD = new Predicate<Entity>()
    {
        public boolean apply(@Nullable Entity p_apply_1_)
        {
            return p_apply_1_ instanceof EntityLivingBase && ((EntityLivingBase)p_apply_1_).getCreatureAttribute()
                    != EnumCreatureAttribute.UNDEAD && !(p_apply_1_ instanceof  EntityHardHerobrine ||
                    p_apply_1_ instanceof EntityCorruptedGhast) &&((EntityLivingBase)p_apply_1_).attackable();
        }
    };

    public EntityHerobrine(World world){
        super(world);
        this.isImmuneToFire = true;
        this.setSize(0.6F, 1.95F);
        this.experienceValue = 250;
        this.secondSpawn = false;
    }

    public EntityHerobrine(World world, boolean secondSpawn) {
        super(world);
        this.isImmuneToFire = true;
        this.setSize(0.6F, 1.95F);
        this.experienceValue = 250;
        this.secondSpawn = secondSpawn;
    }

    protected void initEntityAI()
    {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIHerobrineAttack(this, 2, true));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 32.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false, new Class[0]));

        this.applyEntityAI();
    }

    protected void applyEntityAI()
    {
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, false));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityVillager.class, false));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, false));
        this.targetTasks.addTask(4, new EntityAINearestAttackableTarget(this, EntityTameable.class, false));
        this.targetTasks.addTask(5, new EntityAINearestAttackableTarget(this, EntityMob.class, false));
        this.targetTasks.addTask(6, new EntityAINearestAttackableTarget(this, EntityLivingBase.class, false));
    }


    @Override
    protected void entityInit() {
        super.entityInit();
        this.getDataManager().register(ATTACKING, Boolean.FALSE);
    }

    public void setAttacking(boolean attacking)
    {
        this.getDataManager().set(ATTACKING, Boolean.valueOf(attacking));
    }

    private void setSecondSpawn(boolean second){
        secondSpawn = second;
    }

    public boolean getSecondSpawn()
    {
        return secondSpawn;
    }

    @SideOnly(Side.CLIENT)
    public boolean isAttacking()
    {
        return ((Boolean)this.getDataManager().get(ATTACKING)).booleanValue();
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
    
    public boolean isAIDisabled()
    {
        return false;
    }

    @Override
    public boolean isEntityUndead()
    {
        return true;
    }

    public boolean canDespawn()
    {
        return false;
    }

    public boolean isNonBoss()
    {
        return false;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        if(getAttackTarget()==null && !world.isRemote) {
            if (getAttackTarget() == null) {
                List<EntityPlayer> list = this.world.<EntityPlayer>getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().expand(32.0D, 32.0D, 32.0D));
                for (EntityPlayer entity : list) {
                    if (entity != null && !entity.isCreative())
                        setAttackTarget(entity);
                }
            }
        }

        if(getAnimation() != NO_ANIMATION)
        {
            animationTick++;
            if(world.isRemote && animationTick >= animation.getDuration())
            {
                setAnimation(NO_ANIMATION);
            }
        }

        if(getAttackTarget() != null && !world.isRemote && deathTicks == 0 && currentAnim == null) {
            this.getLookHelper().setLookPositionWithEntity(getAttackTarget(), 10.0F, 10.0F);

            if (rand.nextInt(70) == 1){
                attackEntityWithRangedAttack(getAttackTarget(), 0);
            }

            if(rand.nextInt(50) == 1){
                attackWithBlazeFireballs(getAttackTarget());
            }
        }
        updateAITasks();
    }

    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
        IEntityLivingData data = super.onInitialSpawn(difficulty, livingdata);

        return data;
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();
    }

    protected void updateAITasks()
    {
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

    @Override
    public void setSwingingArms(boolean swingingArms) {}

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float unused) {
        double d1 = 4.0D;
        Vec3d vec3d = this.getLook(1.0F);
        double d2 = target.posX - (this.posX + vec3d.x * 4.0D);
        double d3 = target.getEntityBoundingBox().minY + (double)(target.height / 2.0F) - (0.5D + this.posY + (double)(this.height / 2.0F));
        double d4 = target.posZ - (this.posZ + vec3d.z * 4.0D);
        world.playEvent((EntityPlayer)null, 1016, new BlockPos(this), 0);
        EntityLargeFireball entitylargefireball = new EntityLargeFireball(world, this, d2, d3, d4);
        entitylargefireball.explosionPower = 1;
        entitylargefireball.posX = this.posX;
        entitylargefireball.posY = this.posY + (double)(this.height / 2.0F) - 0.5D;
        entitylargefireball.posZ = this.posZ;

        world.spawnEntity(entitylargefireball);
    }
    
    public void attackWithBlazeFireballs(EntityLivingBase target){

        double d0 = this.getDistanceSq(target);
        double d1 = target.posX - this.posX;
        double d2 = target.getEntityBoundingBox().minY + (double)(target.height / 2.0F) - (this.posY + (double)(this.height / 2.0F));
        double d3 = target.posZ - this.posZ;
        float f = MathHelper.sqrt(MathHelper.sqrt(d0)) * 0.5F;

        for (int i = 0; i < 4; ++i)
        {
            EntitySmallFireball entitysmallfireball = new EntitySmallFireball(this.world, this, d1 + this.getRNG().nextGaussian() * (double)f, d2, d3 + this.getRNG().nextGaussian() * (double)f);
            entitysmallfireball.posY = this.posY + (double)(this.height / 2.0F) + 0.5D;
            this.world.spawnEntity(entitysmallfireball);
        }
    }

    @Override
    public int getAnimationTick()
    {
        return animationTick;
    }

    @Override
    public void setAnimationTick(int tick)
    {
        animationTick = tick;
    }

    @Override
    public Animation getAnimation()
    {
        return this.animation;
    }

    @Override
    public void setAnimation(Animation animation)
    {
        if(animation == NO_ANIMATION)
        {
            onAnimationFinish(this.animation);
            setAnimationTick(0);
        }

        this.animation = animation;
    }

    @Override
    public Animation[] getAnimations()
    {
        return ANIMATIONS;
    }

    protected void onAnimationFinish(Animation animation)
    {}

    public Animation getDeathAnimation()
    {
        return ANIMATION_DEATH;
    }

    protected void onDeathAIUpdate()
    {
        if(getAnimation() != ANIMATION_DEATH)
            AnimationHandler.INSTANCE.sendAnimationMessage(this, ANIMATION_DEATH);
    }

    @Override
    protected void onDeathUpdate() {
        onDeathAIUpdate();
        deathTicks++;

        boolean s;
        if(!world.isRemote){
            s = secondSpawn;
        }
        else
            s = false;

        if(!s)
        {
            if (getAnimation() == NO_ANIMATION && currentAnim == null)
                AnimationHandler.INSTANCE.sendAnimationMessage(this, ANIMATION_DEATH);

            boolean flag = this.world.getGameRules().getBoolean("doMobLoot");
            int i = 250;

            if (!this.world.isRemote) {
                if (this.deathTicks > 15 && this.deathTicks % 5 == 0 && flag) {
                    this.dropExperience(250);
                }
            }

            if (deathTicks == 1) {
                setAnimation(ANIMATION_DEATH);
            }

            if (deathTicks >= 170 && deathTicks % 10 == 0) {
                for (int c = 0; c < 360; c += 15) {
                    EntityLightningBolt lightningBolt = new EntityLightningBolt(world, posX + Math.cos(Math.toRadians(c)), posY, posZ + Math.sin(Math.toRadians(c)), false);
                    world.spawnEntity(lightningBolt);
                }
            }
            if (deathTicks == 199 && !world.isRemote) {
                EntityHardHerobrine hardHerobrine = new EntityHardHerobrine(world);
                hardHerobrine.setLocationAndAngles(posX, posY, posZ, 0, 0);
                hardHerobrine.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
                world.spawnEntity(hardHerobrine);
            }

            if (deathTicks == 200)
                setDead();
        }
        else
            setDead();
    }

    private void dropExperience(int p_184668_1_)
    {
        while (p_184668_1_ > 0)
        {
            int i = EntityXPOrb.getXPSplit(p_184668_1_);
            p_184668_1_ -= i;
            this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, i));
        }
    }

}
