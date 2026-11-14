package net.chargablepipe.keeplooking4ward.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public class SpinParticles extends TextureSheetParticle {

    private final SpriteSet spriteSet;

    private Entity attachedEntity;

    private static final int FRAME_COUNT = 8;
    private static final int FRAME_PER_TICK = 1;
    protected SpinParticles(ClientLevel p_108328_, double p_108329_, double p_108330_, double p_108331_,
                            SpriteSet spriteSet, double p_108332_, double p_108333_, double p_108334_) {
        super(p_108328_, p_108329_, p_108330_, p_108331_, p_108332_, p_108333_, p_108334_);

        this.spriteSet = spriteSet;
        this.friction = 0.5f;
        this.lifetime = 7200;
        this.gravity = 0;

        this.setSprite(spriteSet.get(0, FRAME_COUNT));

        //this.setSpriteFromAge(spriteSet);

        //this.rCol = 255;
        //this.gCol = 255;
        //this.bCol = 255;
    }

    @Override
    public void tick() {
        super.tick();

        if(attachedEntity == null || attachedEntity.isRemoved())
        {
            this.remove();;
            return;
        }
        if(this.removed)
        {
            return;
        }

        this.setPos(attachedEntity.getX(), attachedEntity.getY() + attachedEntity.getBbHeight()/2, attachedEntity.getZ());

        int frame = (this.age * FRAME_PER_TICK) % FRAME_COUNT;

        this.setSprite(spriteSet.get(frame, FRAME_COUNT));
    }

    public void attachTo(Entity entity)
    {
        this.attachedEntity = entity;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
        //return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType>{
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        @Nullable
        public Particle createParticle(SimpleParticleType p_107421_, ClientLevel p_107422_, double p_107423_,
                                       double p_107424_, double p_107425_, double p_107426_,
                                       double p_107427_, double p_107428_) {
            return new SpinParticles(p_107422_, p_107423_, p_107424_, p_107425_, this.spriteSet, p_107426_,
                    p_107427_, p_107428_);
        }

    }

    //public class AttachedParticleOptions implements ParticleOptions
    //{
        //private final int entityId;

        //public AttachedParticleOptions(int entityId)
        //{
            //this.entityId = entityId;
        //}

        //public int getEntityId()
        //{
            //return entityId;
        //}

        //@Override
        //public ParticleType<?> getType()
        //{
            //return ModParticles.SPIN.get();
        //}
    //}
}
