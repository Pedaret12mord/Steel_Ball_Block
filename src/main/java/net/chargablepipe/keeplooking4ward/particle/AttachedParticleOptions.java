package net.chargablepipe.keeplooking4ward.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;

public class AttachedParticleOptions implements ParticleOptions {

    private final int entityId;

    public AttachedParticleOptions(int entityId) {
        this.entityId = entityId;
    }

    public int getEntityId() {
        return this.entityId;
    }

    @Override
    public ParticleType<?> getType() {
        return ModParticles.SPIN.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeVarInt(entityId);
    }

    @Override
    public String writeToString() {
        return Integer.toString(entityId);
    }

    public static final ParticleOptions.Deserializer<AttachedParticleOptions> DESERIALIZER =
            new ParticleOptions.Deserializer<>() {

                @Override
                public AttachedParticleOptions fromCommand(
                        ParticleType<AttachedParticleOptions> type,
                        StringReader reader
                ) throws CommandSyntaxException {
                    return new AttachedParticleOptions(reader.readInt());
                }

                @Override
                public AttachedParticleOptions fromNetwork(
                        ParticleType<AttachedParticleOptions> type,
                        FriendlyByteBuf buffer
                ) {
                    return new AttachedParticleOptions(buffer.readVarInt());
                }
            };
}
