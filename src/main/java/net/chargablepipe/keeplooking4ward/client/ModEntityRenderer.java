package net.chargablepipe.keeplooking4ward.client;

import net.chargablepipe.keeplooking4ward.entity.ModEntities;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
        modid = "keeplooking4ward",
        bus = Mod.EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT
)

public class ModEntityRenderer {


    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(
                ModEntities.STEELBALLPROJECT.get(),
                ThrownItemRenderer::new
        );
        event.registerEntityRenderer(
                ModEntities.WRECKINGBALLPROJECT.get(),
                ThrownItemRenderer::new
        );
        event.registerEntityRenderer(
                ModEntities.SATELLITEPROJECT.get(),
                ThrownItemRenderer::new
        );
    }


}
