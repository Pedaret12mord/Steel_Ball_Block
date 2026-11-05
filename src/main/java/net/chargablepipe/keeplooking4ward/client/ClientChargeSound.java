package net.chargablepipe.keeplooking4ward.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;

public class ClientChargeSound {

    private static ChargeSoundInstance currentSound;

    public static void start(SoundEvent soundEvent, boolean looping, LivingEntity entity) {
        stop();

        currentSound = new ChargeSoundInstance(soundEvent, looping, entity);
        Minecraft.getInstance().getSoundManager().play(currentSound);
    }

    public static void stop() {
        if (currentSound != null) {
            Minecraft.getInstance().getSoundManager().stop(currentSound);
            currentSound.stopSound();
            currentSound = null;
        }
    }

    private static class ChargeSoundInstance extends AbstractTickableSoundInstance {

        private final LivingEntity entity;

        public ChargeSoundInstance(SoundEvent soundEvent, boolean looping, LivingEntity entity) {
            super(soundEvent, SoundSource.PLAYERS, RandomSource.create());

            this.entity = entity;
            this.looping = looping;
            this.volume = 1.0F;
            this.pitch = 1.0F;
        }

        @Override
        public void tick() {
            if (entity.isRemoved()) {
                stop();
                return;
            }

            this.x = entity.getX();
            this.y = entity.getY();
            this.z = entity.getZ();
        }

        public void stopSound() {
            stop();
        }
    }
}
