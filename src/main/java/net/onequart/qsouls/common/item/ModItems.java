package net.onequart.qsouls.common.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.onequart.qsouls.QSouls;
import net.onequart.qsouls.common.item.weapon.*;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, QSouls.MOD_ID);

    public static final RegistryObject<Item> AZURE_NEEDLE = ITEMS.register("azure_needle",
            () -> new AzureNeedleItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> TITAN_RIB = ITEMS.register("titan_rib",
            () -> new TitanRibItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> ECHOING_STINGER = ITEMS.register("echoing_stinger",
            () -> new EchoingStingerItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> GALE_HUNTER = ITEMS.register("gale_hunter",
            () -> new GaleHunterItem(new Item.Properties().stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}