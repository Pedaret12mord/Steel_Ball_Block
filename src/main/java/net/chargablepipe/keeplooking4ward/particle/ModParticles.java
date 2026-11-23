package net.chargablepipe.keeplooking4ward.particle;

import com.mojang.serialization.Codec;
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

    public static final RegistryObject<ParticleType<AttachedParticleOptions>> SPIN =
            PARTICLE_TYPES.register("spin", () ->
                    new ParticleType<AttachedParticleOptions>(
                            false,
                            AttachedParticleOptions.DESERIALIZER
                    ) {
                        @Override
                        public Codec<AttachedParticleOptions> codec() {
                            return Codec.INT.xmap(
                                    AttachedParticleOptions::new,
                                    AttachedParticleOptions::getEntityId
                            );
                        }
                    }
            );

    public static final RegistryObject<ParticleType<AttachedRippleParticleOptions>> RIPPLE =
            PARTICLE_TYPES.register("ripple", () ->
                    new ParticleType<AttachedRippleParticleOptions>(
                            false,
                            AttachedRippleParticleOptions.DESERIALIZER
                    ) {
                        @Override
                        public Codec<AttachedRippleParticleOptions> codec() {
                            return Codec.INT.xmap(
                                    AttachedRippleParticleOptions::new,
                                    AttachedRippleParticleOptions::getEntityId
                            );
                        }
                    }
            );


    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}
