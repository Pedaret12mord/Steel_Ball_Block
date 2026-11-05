package net.chargablepipe.keeplooking4ward.entity;

import net.chargablepipe.keeplooking4ward.KeepLooking4ward;
import net.chargablepipe.keeplooking4ward.entity.custom.SatelliteProjectile;
import net.chargablepipe.keeplooking4ward.entity.custom.SteelBallProjectile;
import net.chargablepipe.keeplooking4ward.entity.custom.WreckingBallProjectile;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, KeepLooking4ward.MOD_ID);

    public static final RegistryObject<EntityType<SteelBallProjectile>> STEELBALLPROJECT =
            ENTITY_TYPES.register("steel_ball_project", () -> EntityType.Builder.<SteelBallProjectile>of(SteelBallProjectile::new, MobCategory.MISC)
                    .sized(0.4f, 0.4f).build("steel_ball_run"));

    public static final RegistryObject<EntityType<WreckingBallProjectile>> WRECKINGBALLPROJECT =
            ENTITY_TYPES.register("wrecking_ball_project", () -> EntityType.Builder.<WreckingBallProjectile>of(WreckingBallProjectile::new, MobCategory.MISC)
                    .sized(0.4f, 0.4f).build("steel_ball_run"));

    public static final RegistryObject<EntityType<SatelliteProjectile>> SATELLITEPROJECT =
            ENTITY_TYPES.register("satellite_project", () -> EntityType.Builder.<SatelliteProjectile>of(SatelliteProjectile::new, MobCategory.MISC)
                    .sized(0.1f, 0.1f).build("steel_ball_run"));

    public static void register(IEventBus eventBus) { ENTITY_TYPES.register(eventBus);}
}
