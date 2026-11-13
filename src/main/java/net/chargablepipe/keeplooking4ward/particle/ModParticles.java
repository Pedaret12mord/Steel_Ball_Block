package net.chargablepipe.keeplooking4ward.particle;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModParticles {
    public static final DeferredRegister<net.minecraft.core.particles.ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(
                    ForgeRegistries.PARTICLE_TYPES,
                    keeplooking4ward.ModId
            )
}
