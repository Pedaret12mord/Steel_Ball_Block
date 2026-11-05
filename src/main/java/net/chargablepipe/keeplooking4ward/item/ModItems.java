package net.chargablepipe.keeplooking4ward.item;
import net.chargablepipe.keeplooking4ward.KeepLooking4ward;
import net.chargablepipe.keeplooking4ward.item.custom.SteelBallItem;
import net.chargablepipe.keeplooking4ward.item.custom.SatelliteItem;
import net.chargablepipe.keeplooking4ward.item.custom.WreckingItem;
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

    public static final RegistryObject<Item> WRECKING_BALL = ITEMS.register("wrecking_ball",
            () -> new WreckingItem(
                    new Item.Properties().stacksTo(1)
            ));

    public static final RegistryObject<Item> SATELLITE_FUNC = ITEMS.register("satellite2",
            () -> new SatelliteItem(
                    new Item.Properties()
            ));

    public  static void register(IEventBus eventBus)
    {
        ITEMS.register(eventBus);
    }
}
