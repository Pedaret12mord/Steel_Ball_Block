package net.chargablepipe.keeplooking4ward.entity.custom;

import net.chargablepipe.keeplooking4ward.entity.ModEntities;
import net.chargablepipe.keeplooking4ward.item.ModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SatelliteProjectile extends ThrowableItemProjectile {

    private Vec3 startPosition;

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
    protected Item getDefaultItem() {
        return ModItems.SATELLITE_FUNC.get();
    }

    @Override
    protected float getGravity() {
        return 0;
    }
}
