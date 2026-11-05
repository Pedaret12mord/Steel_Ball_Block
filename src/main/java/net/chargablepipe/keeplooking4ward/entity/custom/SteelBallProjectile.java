package net.chargablepipe.keeplooking4ward.entity.custom;

import net.chargablepipe.keeplooking4ward.entity.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.chargablepipe.keeplooking4ward.item.ModItems;
import net.minecraft.world.phys.EntityHitResult;

public class SteelBallProjectile extends ThrowableItemProjectile {
    private Vec3 startPosition;
    private Vec3 slowdownStartVelocity;

    private enum FlightPhase {
        OUTBOUND,
        SLOWING_DOWN,
        STUCK,
        WEAKBLOCKSTOCK,
        STRONGBLOCKSTOCK,
        RETURNING
    }

    private FlightPhase phase = FlightPhase.OUTBOUND;
    private FlightPhase previousPhase = phase;

    private int slowdownTick = 0;

    // Configuration
    private final double outboundDistance = 25.0;
    private final int slowdownDuration = 15;
    private final double returnSpeed = 1.2;

    private Entity stuckEntity;
    private BlockState stuckBlock;

    private BlockPos blockPosStuck;

    private int stuckTicks = 0;
    private int blockStuckTick = 0;

    private final int stuckDuration = 100; // 5 seconds
    private final int hardBlockStuckDuration = 100;
    public double blockStuckDuration;
    private final int damageInterval = 10; // Damage every second
    private final float stuckDamage = 1.0F; // 1 heart
    public double wholeDistance;
    public boolean hasStuckBefore;

    @Override
    public void tick() {
        switch (phase) {

            // PHASE 1: Fly forward
            case OUTBOUND -> {
                super.tick();


                double distance;
                if(!hasStuckBefore)
                {
                    distance = this.position().distanceTo(startPosition);
                    wholeDistance = distance;
                }
                else
                {
                    //distance = this.position().distanceTo(startPosition) + wholeDistance;
                    distance = this.position().distanceTo(startPosition);
                    //wholeDistance += this.position().distanceTo(startPosition);
                }

                if (distance >= outboundDistance) {
                    phase = FlightPhase.SLOWING_DOWN;
                    slowdownStartVelocity = this.getDeltaMovement();
                }
            }

            // PHASE 2: Smoothly slow down
            case SLOWING_DOWN -> {
                previousPhase = phase;

                slowdownTick++;

                double t = Math.min(
                        slowdownTick / (double) slowdownDuration,
                        1.0
                );

                // Smoothstep interpolation
                double smooth = t * t * (3.0 - 2.0 * t);

                // Gradually reduce velocity
                this.setDeltaMovement(
                        slowdownStartVelocity.scale(1.0 - smooth)
                );

                super.tick();

                if (slowdownTick >= slowdownDuration) {
                    this.setDeltaMovement(Vec3.ZERO);
                    phase = FlightPhase.RETURNING;
                }
            }

            case STUCK -> {

                super.tick();

                if (stuckEntity == null || !stuckEntity.isAlive()) {
                    phase = FlightPhase.RETURNING;
                    return;
                }

                stuckTicks++;

                // Attach the projectile to the entity

                Vec3 attachedPosition = stuckEntity.position().add(
                        0,
                        stuckEntity.getBbHeight() * 0.5,
                        0
                );

                this.setPos(attachedPosition);

                // Immobilize the entity
                stuckEntity.setDeltaMovement(new Vec3(0,stuckEntity.getDeltaMovement().y,0));

                // Damage the entity periodically (server only)
                if (!this.level().isClientSide() && stuckTicks % damageInterval == 0) {

                    stuckEntity.hurt(
                            this.damageSources().thrown(this, this.getOwner()),
                            stuckDamage
                    );
                }

                // Time to return
                if (stuckTicks >= stuckDuration) {
                    phase = FlightPhase.RETURNING;
                }
            }

            case WEAKBLOCKSTOCK -> {
                super.tick();
                if (stuckBlock == null)
                {
                    phase = previousPhase;
                    return;
                }
                blockStuckTick++;

                // Attach the projectile to the entity
                Vec3 attachedPosition = blockPosStuck.getCenter();

                this.setPos(attachedPosition);


                // Time to return
                if (blockStuckTick >= blockStuckDuration) {


                    level().destroyBlock(blockPosStuck, true);

                    phase = previousPhase;
                }

            }

            case STRONGBLOCKSTOCK -> {
                super.tick();
                if (stuckBlock == null)
                {
                    phase = FlightPhase.RETURNING;
                    return;
                }
                blockStuckTick++;

                // Attach the projectile to the entity
                Vec3 attachedPosition = blockPosStuck.getCenter();


                this.setPos(attachedPosition);
                if(!(level().getBlockState(blockPosStuck)).isAir())
                {
                    level().addDestroyBlockEffect(blockPosStuck, level().getBlockState(blockPosStuck));
                }


                // Time to return
                if (blockStuckTick >= hardBlockStuckDuration) {
                    //stuckBlock.getBlock().destroy(level(), blockPosStuck, stuckBlock);
                    level().destroyBlock(blockPosStuck, true);
                    blockStuckTick = 0;
                    phase = FlightPhase.RETURNING;
                }
            }

            // PHASE 3: Return to the player
            case RETURNING -> {
                previousPhase = phase;
                Entity owner = this.getOwner();

                if (owner == null || !owner.isAlive()) {
                    this.discard();
                    return;
                }

                Vec3 target = owner.position().add(
                        0,
                        owner.getBbHeight() * 0.5,
                        0
                );

                Vec3 direction = target.subtract(this.position());

                // Reached the player
                if (direction.length() < 1.0) {
                    if(!this.level().isClientSide)
                    {
                        if(owner instanceof Player player)
                        {
                            ItemStack returnedBomb = new ItemStack(ModItems.BOMB.get());
                            if(!player.getInventory().add(returnedBomb))
                            {
                                player.drop(returnedBomb,false);
                            }
                        }
                    }
                    this.discard();
                    return;
                }

                // Move toward the player
                this.setDeltaMovement(
                        direction.normalize().scale(returnSpeed)
                );

                super.tick();
            }
        }
    }

    public SteelBallProjectile(EntityType<? extends ThrowableItemProjectile> p_37442_, Level p_37443_) {
        super(p_37442_, p_37443_);
        this.startPosition = this.position();
    }

    public SteelBallProjectile(Level p_37443_) {
        super(ModEntities.STEELBALLPROJECT.get(), p_37443_);
        this.startPosition = this.position();
    }

    public SteelBallProjectile(Level p_37443_, LivingEntity p_37439_) {
        super(ModEntities.STEELBALLPROJECT.get(), p_37439_ , p_37443_);
        this.startPosition = this.position();
    }


    @Override
    protected Item getDefaultItem() {
        return ModItems.BOMB.get();
    }

    @Override
    protected float getGravity() {
        return 0;
    }

    @Override
    protected void onHitBlock(BlockHitResult p_37258_) {
        if (!this.level().isClientSide())
        {
            this.level().broadcastEntityEvent(this, (byte) 3);
        }

        super.onHitBlock(p_37258_);

        hasStuckBefore = true;
        blockPosStuck = p_37258_.getBlockPos();
        stuckBlock = level().getBlockState(blockPosStuck);
        if(stuckBlock.getDestroySpeed(level(), blockPosStuck)>= 1.5f)
        {
            phase = FlightPhase.STRONGBLOCKSTOCK;
        }
        else
        {
            blockStuckDuration = stuckBlock.getDestroySpeed(level(), blockPosStuck)*10;
            phase = FlightPhase.WEAKBLOCKSTOCK;
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult p_37259_) {
        if (!this.level().isClientSide())
        {
            this.level().broadcastEntityEvent(this, (byte) 3);
        }
        //if (phase == FlightPhase.RETURNING) {
            //return;
        //

        stuckEntity = p_37259_.getEntity();

        if (stuckEntity == this.getOwner()) {
            return;
        }

        stuckEntity.hurt(this.damageSources().thrown(this, this.getOwner()),2);

        phase = FlightPhase.STUCK;
        stuckTicks = 0;

        // Disable projectile collision and movement physics
        //this.setNoPhysics(true);
        //this.no
        this.setDeltaMovement(Vec3.ZERO);
    }
}
