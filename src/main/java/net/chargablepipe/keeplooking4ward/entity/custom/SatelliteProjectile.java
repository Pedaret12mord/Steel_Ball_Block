package net.chargablepipe.keeplooking4ward.entity.custom;

import net.chargablepipe.keeplooking4ward.entity.ModEntities;
import net.chargablepipe.keeplooking4ward.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
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
    private float hitDamage = 3;
    private int effectDuration = 100;
    private int lifeTime = 50;
    private int lifeTimeTick =0;

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

    @Override
    public void tick() {
        super.tick();
        lifeTimeTick ++;
        if(lifeTimeTick>= lifeTime)
        {
            this.discard();
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


