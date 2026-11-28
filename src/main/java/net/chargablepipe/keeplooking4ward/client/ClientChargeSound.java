package net.chargablepipe.keeplooking4ward.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClientChargeSound {

    private static final Map<String, ChargeSoundInstance> sounds = new HashMap<>();

    public static void start(
            String soundID,
            SoundEvent soundEvent,
            boolean looping,
            Entity entity
    ) {
        // Don't start if this ID is already being used
        if (sounds.containsKey(soundID)) {
            return;
        }

        ChargeSoundInstance sound = new ChargeSoundInstance(
                soundEvent,
                looping,
                entity
        );

        sounds.put(soundID, sound);

        Minecraft.getInstance()
                .getSoundManager()
                .play(sound);
    }

    public static void stop(String soundID) {

        ChargeSoundInstance sound = sounds.remove(soundID);

        if (sound != null) {
            sound.stopSound();
        }
    }

    private static class ChargeSoundInstance extends AbstractTickableSoundInstance {

        private final Entity entity;

        public void stopSound() {
            this.stop();
        }

        public ChargeSoundInstance(SoundEvent soundEvent, boolean looping, Entity entity) {

            super(soundEvent, SoundSource.PLAYERS, RandomSource.create());

            this.entity = entity;

            this.looping = looping;
            this.delay = 0;
            this.volume = 3.0F;
            this.pitch = 1.0F;

            this.x = entity.getX();
            this.y = entity.getY();
            this.z = entity.getZ();
        }

        @Override
        public void tick() {

            if (entity.isRemoved()) {
                this.stop();
                return;
            }

            this.x = entity.getX();
            this.y = entity.getY();
            this.z = entity.getZ();
        }
    }
}