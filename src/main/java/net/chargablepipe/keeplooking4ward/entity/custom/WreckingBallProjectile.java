package net.chargablepipe.keeplooking4ward.entity.custom;

import net.chargablepipe.keeplooking4ward.client.ClientChargeSound;
import net.chargablepipe.keeplooking4ward.entity.ModEntities;
import net.chargablepipe.keeplooking4ward.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class WreckingBallProjectile extends ThrowableItemProjectile {
    private Vec3 startPosition;
    private Vec3 slowdownStartVelocity;

    private enum FlightPhase {
        OUTBOUND,
        SLOWING_DOWN,
        WEAKBLOCKSTOCK,
        STRONGBLOCKSTOCK,
        RETURNING,
        STOPPING
    }

    private FlightPhase phase = FlightPhase.OUTBOUND;
    private FlightPhase previousPhase = phase;

    private int slowdownTick = 0;

    // Configuration
    private final double outboundDistance = 7.0;
    private final int slowdownDuration = 15;
    private final int stopDuration = 45;
    private final double returnSpeed = 1.2;

    private Entity stuckEntity;
    private BlockState stuckBlock;

    private BlockPos blockPosStuck;

    private int stopTicks = 0;
    private int blockStuckTick = 0;

    private final int hardBlockStuckDuration = 100;
    public double blockStuckDuration;
    public double wholeDistance;
    public boolean hasStuckBefore;

    private int maxSatellites = 14;
    private int satellitesSpawned;
    private int satelliteSpawnDelay = 1;

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
                    phase = FlightPhase.STOPPING;
                    //spawnSatellites();
                }
            }

            case STOPPING -> {
                super.tick();
                stopTicks++;

                if (!level().isClientSide()) {
                    if (satellitesSpawned < maxSatellites && tickCount % satelliteSpawnDelay == 0) {
                        spawnSatellites();
                        satellitesSpawned++;
                    }
                }

                // Time to return
                if (stopTicks>= stopDuration) {
                    phase = WreckingBallProjectile.FlightPhase.RETURNING;
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
                            ItemStack returnedBomb = new ItemStack(ModItems.WRECKING_BALL.get());
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


    public WreckingBallProjectile(EntityType<? extends ThrowableItemProjectile> p_37442_, Level p_37443_) {
        super(p_37442_, p_37443_);
        this.startPosition = this.position();
    }

    public WreckingBallProjectile(Level p_37443_) {
        super(ModEntities.WRECKINGBALLPROJECT.get(), p_37443_);
        this.startPosition = this.position();
    }

    public WreckingBallProjectile(Level p_37443_, LivingEntity p_37439_) {
        super(ModEntities.WRECKINGBALLPROJECT.get(), p_37439_ , p_37443_);
        this.startPosition = this.position();
    }

    private Vec3 getRandomDownwardDirection() {
        RandomSource random = level().getRandom();

        //double y = -random.nextDouble(); // Between -1 and 0
        //double y = 0.25 - random.nextDouble() * 1.25;
        double y = random.nextDouble() - 0.75;
        double angle = random.nextDouble() * Math.PI * 2;

        double horizontal = Math.sqrt(1 - y * y);

        return new Vec3(
                Math.cos(angle) * horizontal,
                y,
                Math.sin(angle) * horizontal
        );
    }

    private void spawnSatellites() {
        if (level().isClientSide()) return;

        //for (int i = 0; i < 14; i++) {
            SatelliteProjectile satellite = new SatelliteProjectile(
                    ModEntities.SATELLITEPROJECT.get(),
                    level()
            );

            satellite.setPos(position());

            Vec3 direction = getRandomDownwardDirection();

            satellite.setDeltaMovement(direction.scale(0.85));

            satellite.setOwner(getOwner());

            level().addFreshEntity(satellite);

        net.minecraftforge.fml.DistExecutor.unsafeRunWhenOn(
                net.minecraftforge.api.distmarker.Dist.CLIENT,
                () -> () -> ClientChargeSound.start(SoundEvents.ITEM_PICKUP, false, (LivingEntity) getOwner()

                )
        );
        //}
    }


    @Override
    protected Item getDefaultItem() {
        return ModItems.WRECKING_BALL.get();
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

        stuckEntity = p_37259_.getEntity();

        if (stuckEntity == this.getOwner()) {
            return;
        }

        stuckEntity.hurt(this.damageSources().thrown(this, this.getOwner()),2);

    }
}
