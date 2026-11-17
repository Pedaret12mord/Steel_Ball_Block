package net.chargablepipe.keeplooking4ward.entity.custom;

import net.chargablepipe.keeplooking4ward.entity.ModEntities;
import net.chargablepipe.keeplooking4ward.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class SatelliteProjectile extends ThrowableItemProjectile {

    private Vec3 startPosition;
    private float hitDamage = 5;
    private int effectDuration = 100;
    private int lifeTime = 50;
    private int lifeTimeTick =0;

    private Mob homingTarget = null;
    private boolean trackingEnded = false;

    private final double detectionRadius = 4;
    private final double maxTrackingRange = 9;

    private final double turnStrength = 1;


    public enum FlightPhase {
        OUTBOUND,
        HOMING
    }

    private FlightPhase phase = FlightPhase.OUTBOUND;

    public SatelliteProjectile(EntityType<? extends ThrowableItemProjectile> p_37442_, Level p_37443_) {
        super(p_37442_, p_37443_);
        this.startPosition = this.position();
    }

    public SatelliteProjectile(Level p_37443_) {
        super(ModEntities.SATELLITEPROJECT.get(), p_37443_);
        this.startPosition = this.position();
    }

    public SatelliteProjectile(Level p_37443_, LivingEntity p_37439_) {
        super(ModEntities.SATELLITEPROJECT.get(), p_37439_ , p_37443_);
        this.startPosition = this.position();
    }

    private void findTarget()
    {
        if(trackingEnded || homingTarget != null )
        {
            return;
        }

        Vec3 velocity = getDeltaMovement();

        if(velocity.lengthSqr() < 0.0001)
            return;

        Vec3 direction = velocity.normalize();

        double closestDistance = detectionRadius * detectionRadius;

        for(Mob mob : level().getEntitiesOfClass(
                Mob.class,
                getBoundingBox().inflate(detectionRadius)))
        {
            if(!mob.isAlive() || mob == getOwner())
                continue;
            Vec3 targetPosition = mob.position().add(0, mob.getBbHeight() * 0.5, 0);

            Vec3 toTarget = targetPosition.subtract(position());

            double distance = toTarget.lengthSqr();

            if(distance > closestDistance)
                continue;
            double dot = direction.dot(toTarget.normalize());

            if(dot < 0.5)
                continue;

            closestDistance = distance;
            homingTarget = mob;
            phase = FlightPhase.HOMING;
        }
    }

    private void homeTowardsTarget()
    {
        if (homingTarget == null)
            return;

        if (!homingTarget.isAlive()) {
            homingTarget = null;
            trackingEnded = true;
            phase = FlightPhase.OUTBOUND;
            return;
        }

        Vec3 currentVelocity = getDeltaMovement();

        double speed = currentVelocity.length();

        if (speed < 0.0001)
            return;

        // Check target distance
        if (distanceTo(homingTarget) > maxTrackingRange) {

            homingTarget = null;
            trackingEnded = true;
            phase = FlightPhase.OUTBOUND;
            return;
        }

        // Direction towards target
        Vec3 targetPosition = homingTarget.position()
                .add(0, homingTarget.getBbHeight() * 0.5, 0);

        Vec3 desiredDirection = targetPosition
                .subtract(position())
                .normalize();

        // Current direction
        Vec3 currentDirection = currentVelocity.normalize();

        // Gradually turn towards target
        Vec3 newDirection = currentDirection
                .lerp(desiredDirection, turnStrength)
                .normalize();

        // Preserve projectile speed
        setDeltaMovement(newDirection.scale(speed));
    }

    @Override
    public void tick() {
        switch (phase) {

            // PHASE 1: Fly forward
            case OUTBOUND -> {
                super.tick();

                if (!level().isClientSide) {

                    findTarget();

                }

                lifeTimeTick ++;
                if(lifeTimeTick>= lifeTime)
                {
                    this.discard();
                }
            }
            case HOMING -> {
                super.tick();

                if (!level().isClientSide) {

                    homeTowardsTarget();

                }
            }
        }

    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.SATELLITE_FUNC.get();
    }

    @Override
    protected float getGravity() {
        return 0;
    }

    @Override
    protected void onHitEntity(EntityHitResult p_37259_) {
        if (!this.level().isClientSide())
        {
            this.level().broadcastEntityEvent(this, (byte) 3);
        }

        Entity hitEntity = p_37259_.getEntity();
        hitEntity.hurt(this.damageSources().generic(), hitDamage);

        if (!level().isClientSide() && hitEntity instanceof LivingEntity living) {
            living.addEffect(
                    new MobEffectInstance(MobEffects.BLINDNESS, effectDuration, 2)
            );
        }
        super.onHitEntity(p_37259_);
        this.discard();

    }

    protected void onHitBlock(BlockHitResult p_37258_) {
        if (!this.level().isClientSide())
        {
            this.level().broadcastEntityEvent(this, (byte) 3);
        }

        super.onHitBlock(p_37258_);

        BlockPos blockPosStuck = p_37258_.getBlockPos();

       BlockState stuckBlock = level().getBlockState(blockPosStuck);
        if(stuckBlock.getDestroySpeed(level(), blockPosStuck)< 1.5f)
        {
            level().destroyBlock(blockPosStuck, true);
        }
        else
        {
            if(!(level().getBlockState(blockPosStuck)).isAir())
            {
                level().addDestroyBlockEffect(blockPosStuck, level().getBlockState(blockPosStuck));
            }
        }
        this.discard();
    }

}


