package net.chargablepipe.keeplooking4ward.item;
import net.chargablepipe.keeplooking4ward.KeepLooking4ward;
import net.chargablepipe.keeplooking4ward.item.custom.*;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, KeepLooking4ward.MOD_ID);

    //public static final RegistryObject<Item> STEELBALL = ITEMS.register("steel_ball",
            //() -> new Item(new Item.Properties()));

    //public static final RegistryObject<Item> WRECKING_BALL = ITEMS.register("wrecking_ball",
            //() -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> Satellite = ITEMS.register("satellite",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> STEELBALLBASE = ITEMS.register("steel_ball_base",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> STEELBALL = ITEMS.register("steel_ball",
                    () -> new SteelBallItem(
                            new Item.Properties().stacksTo(1)
                    ));

    public static final RegistryObject<Item> BOMB = ITEMS.register("steel_ball_bomb",
            () -> new SteelBallItem(
                    new Item.Properties().stacksTo(1)
            ));

    //public static final RegistryObject<Item> STEELBALLTHREED = ITEMS.register("steel_ball_3d",
            //() -> new SteelBallNormalItem(
                    //new Item.Properties().stacksTo(1)
            //));

    public static final RegistryObject<Item> STEELBALLTHREED = ITEMS.register("steel_ball_normal",
            () -> new SteelBallNormalItem(
                    new Item.Properties().stacksTo(1)
            ));


    public static final RegistryObject<Item> WRECKING_BALL = ITEMS.register("wrecking_ball",
            () -> new WreckingItem(
                    new Item.Properties().stacksTo(1)
            ));

    public static final RegistryObject<Item> SATELLITE_FUNC = ITEMS.register("satellite2",
            () -> new SatelliteItem(
                    new Item.Properties()
            ));

    //public static final RegistryObject<Item> ANIMATED_BALL = ITEMS.register("animated_ball",
            //() -> new Item( new Item.Properties()
            //));

    public static final RegistryObject<Item> ANIMATED_BALL =
            ITEMS.register("animated_ball",
                    () -> new AnimatedBallItem(
                            new Item.Properties().stacksTo(1)
                    ));

    public  static void register(IEventBus eventBus)
    {
        ITEMS.register(eventBus);
    }
}
