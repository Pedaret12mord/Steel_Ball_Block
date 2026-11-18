package net.chargablepipe.keeplooking4ward.sound;

import net.chargablepipe.keeplooking4ward.KeepLooking4ward;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.ForgeSoundType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, KeepLooking4ward.MOD_ID);

    public static final RegistryObject<SoundEvent> SPINSOUND = registerSoundEvents("spin_sound");

    public static final RegistryObject<SoundEvent> SPINHITSOUND = registerSoundEvents("spin_hit_sound");

    public static final RegistryObject<SoundEvent> SPINTHROWSOUND = registerSoundEvents("spin_throw_sound");

    public static final RegistryObject<SoundEvent> SOUND_BLOCK_BREAK = registerSoundEvents("sound_block_break");
    public static final RegistryObject<SoundEvent> SOUND_BLOCK_STEP = registerSoundEvents("sound_block_step");
    public static final RegistryObject<SoundEvent> SOUND_BLOCK_FALL = registerSoundEvents("sound_block_fall");
    public static final RegistryObject<SoundEvent> SOUND_BLOCK_PLACE = registerSoundEvents("sound_block_place");
    public static final RegistryObject<SoundEvent> SOUND_BLOCK_HIT = registerSoundEvents("sound_block_hit");

    private static RegistryObject<SoundEvent> registerSoundEvents(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(KeepLooking4ward.MOD_ID, name)));
    }

    public static final ForgeSoundType SOUND_STEEL_BALL_SOUNDS = new ForgeSoundType(1f, 1f,
            ModSounds.SOUND_BLOCK_BREAK, ModSounds.SOUND_BLOCK_STEP, ModSounds.SOUND_BLOCK_FALL,
            ModSounds.SOUND_BLOCK_PLACE, ModSounds.SOUND_BLOCK_HIT);

    public static void register(IEventBus eventBus)
    {
        SOUND_EVENTS.register(eventBus);
    }
}
