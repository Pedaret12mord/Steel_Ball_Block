package net.chargablepipe.keeplooking4ward.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public class RippleParticles extends TextureSheetParticle {

    private final SpriteSet spriteSet;

    private Entity attachedEntity;

    private static final int FRAME_COUNT = 3;
    private static final int FRAME_PER_TICK = 1;

    private final int entityId;
    private int waitTicks = 0;
    private boolean hasAttached = false;

    protected RippleParticles(ClientLevel p_108328_,
                              double p_108329_,
                              double p_108330_,
                              double p_108331_,
                              SpriteSet spriteSet,
                              double p_108332_,
                              double p_108333_,
                              double p_108334_,
                              int entityId) {
        super(p_108328_, p_108329_, p_108330_, p_108331_, p_108332_, p_108333_, p_108334_);

        this.spriteSet = spriteSet;
        this.friction = 0.5f;
        this.lifetime = 7200;
        this.gravity = 0;
        this.entityId = entityId;


        this.setSprite(spriteSet.get(0, FRAME_COUNT));

        this.quadSize = 0.25f;

        //this.setSpriteFromAge(spriteSet);

        //this.rCol = 255;
        //this.gCol = 255;
        //this.bCol = 255;
        this.alpha = 0.3f;
    }

    //@Override
    //protected int getLightColor(float p_107249_) {
        //return super.getLightColor(p_107249_);
        //return LightTexture.pack(15, 0);
        //return 1000;
    //}

    @Override
    public void tick() {
        super.tick();

        if (this.removed) return;

        attachedEntity = this.level.getEntity(entityId);

        if (attachedEntity == null) {
            if (hasAttached || ++waitTicks > 20) {
                this.remove();
            }
            return;
        }

        if (attachedEntity.isRemoved()) {
            this.remove();
            return;
        }

        hasAttached = true;

        this.setPos(attachedEntity.getX(), attachedEntity.getY() - (1.3*attachedEntity.getBbHeight()), attachedEntity.getZ());

        int frame = (this.age * FRAME_PER_TICK) % FRAME_COUNT;

        this.setSprite(spriteSet.get(frame, FRAME_COUNT));
    }

    public void attachTo(Entity entity)
    {
        this.attachedEntity = entity;
    }

    @Override
    public ParticleRenderType getRenderType() {
        //return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
        return ParticleRenderType.PARTICLE_SHEET_LIT;
        //return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public static class Provider implements ParticleProvider<AttachedRippleParticleOptions>{
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        @Nullable
        public Particle createParticle(
                AttachedRippleParticleOptions p_107421_,
                ClientLevel p_107422_,
                double p_107423_,
                double p_107424_,
                double p_107425_,
                double p_107426_,
                double p_107427_,
                double p_107428_
        )
        {
            return new RippleParticles(
                    p_107422_,
                    p_107423_,
                    p_107424_,
                    p_107425_,
                    this.spriteSet,
                    p_107426_,
                    p_107427_,
                    p_107428_,
                    p_107421_.getEntityId()
            );
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
