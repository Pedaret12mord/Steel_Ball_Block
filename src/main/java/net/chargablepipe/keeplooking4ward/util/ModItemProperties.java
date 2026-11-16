package net.chargablepipe.keeplooking4ward.util;

import net.chargablepipe.keeplooking4ward.KeepLooking4ward;
import net.chargablepipe.keeplooking4ward.item.ModItems;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(
        modid = KeepLooking4ward.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT
)
public class ModItemProperties {

    @SubscribeEvent
    public static void registerItemProperties(FMLClientSetupEvent event) {

        event.enqueueWork(() -> {

            ItemProperties.register(
                    ModItems.STEELBALL.get(),
                    new ResourceLocation(KeepLooking4ward.MOD_ID, "charging"),
                    (stack, level, entity, seed) -> {
                        return entity != null
                                && entity.isUsingItem()
                                && entity.getUseItem() == stack
                                ? 1.0F : 0.0F;
                    }
            );

        });
    }
}
