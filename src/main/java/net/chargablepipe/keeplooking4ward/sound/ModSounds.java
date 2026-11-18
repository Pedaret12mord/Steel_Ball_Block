package net.chargablepipe.keeplooking4ward.sound;

import net.chargablepipe.keeplooking4ward.KeepLooking4ward;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENT =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, KeepLooking4ward.MOD_ID);

    public static void register(IEventBus eventBus)
    {

    }
}
