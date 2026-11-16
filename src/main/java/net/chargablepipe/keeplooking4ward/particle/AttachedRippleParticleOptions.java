package net.chargablepipe.keeplooking4ward.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;

public class AttachedRippleParticleOptions implements ParticleOptions {

    private final int entityId;

    public AttachedRippleParticleOptions(int entityId) {
        this.entityId = entityId;
    }

    public int getEntityId() {
        return this.entityId;
    }

    @Override
    public ParticleType<?> getType() {
        return ModParticles.RIPPLE.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeVarInt(entityId);
    }

    @Override
    public String writeToString() {
        return Integer.toString(entityId);
    }

    public static final Deserializer<AttachedRippleParticleOptions> DESERIALIZER =
            new Deserializer<>() {

                @Override
                public AttachedRippleParticleOptions fromCommand(
                        ParticleType<AttachedRippleParticleOptions> type,
                        StringReader reader
                ) throws CommandSyntaxException {
                    return new AttachedRippleParticleOptions(reader.readInt());
                }

                @Override
                public AttachedRippleParticleOptions fromNetwork(
                        ParticleType<AttachedRippleParticleOptions> type,
                        FriendlyByteBuf buffer
                ) {
                    return new AttachedRippleParticleOptions(buffer.readVarInt());
                }
            };
}
