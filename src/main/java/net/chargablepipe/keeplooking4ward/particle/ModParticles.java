package net.chargablepipe.keeplooking4ward.particle;

import net.chargablepipe.keeplooking4ward.KeepLooking4ward;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(
                    ForgeRegistries.PARTICLE_TYPES,
                    KeepLooking4ward.MOD_ID
            );

    public static final RegistryObject<SimpleParticleType> SPIN =
            PARTICLE_TYPES.register("spin", () -> new SimpleParticleType(true));

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}
