package net.chargablepipe.keeplooking4ward;

import com.mojang.logging.LogUtils;
import net.chargablepipe.keeplooking4ward.entity.ModEntities;
import net.chargablepipe.keeplooking4ward.item.ModCreativeModTabs;
import net.chargablepipe.keeplooking4ward.item.ModItems;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(KeepLooking4ward.MOD_ID)
public class KeepLooking4ward
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "keeplooking4ward";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public KeepLooking4ward()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModCreativeModTabs.register(modEventBus);

        ModEntities.register(FMLJavaModLoadingContext.get().getModEventBus());

        ModItems.register(modEventBus);
        //IEventBus modEventBus = context.getModEventBus();

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);


    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if(event.getTabKey() == CreativeModeTabs.COMBAT)
        {
            //event.accept(ModItems.STEELBALL);
            //event.accept(ModItems.WRECKING_BALL);
            event.accept(ModItems.BOMB);
        }
        if(event.getTabKey() == CreativeModeTabs.INGREDIENTS)
        {
            event.accept(ModItems.Satellite);
            event.accept(ModItems.STEELBALLBASE);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {

        }
    }
}
