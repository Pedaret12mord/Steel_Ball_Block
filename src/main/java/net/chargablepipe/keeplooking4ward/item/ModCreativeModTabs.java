package net.chargablepipe.keeplooking4ward.item;

import net.chargablepipe.keeplooking4ward.KeepLooking4ward;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModTabs
{
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, KeepLooking4ward.MOD_ID);

    public static final RegistryObject<CreativeModeTab> STEEL_BALL_RUN_TAB = CREATIVE_MODE_TABS.register("steel_ball_run_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.STEELBALL.get()))
                    .title(Component.translatable("creativetab.steel_ball_run_tab"))
                    .displayItems((p_270258_, p_259752_) -> {
                        p_259752_.accept(ModItems.STEELBALL.get());
                        p_259752_.accept(ModItems.WRECKING_BALL.get());
                        p_259752_.accept(ModItems.Satellite.get());
                        p_259752_.accept(ModItems.STEELBALLBASE.get());
                        p_259752_.accept(ModItems.BOMB.get());
                        p_259752_.accept(ModItems.WRECKING_BALL_FUNC.get());
                    })
                    .build());

    public static void register(IEventBus eventBus)
    {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
