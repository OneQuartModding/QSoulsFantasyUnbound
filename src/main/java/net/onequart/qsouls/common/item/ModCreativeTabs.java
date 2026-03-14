package net.onequart.qsouls.common.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.onequart.qsouls.QSouls;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, QSouls.MOD_ID);

    public static final RegistryObject<CreativeModeTab> QSOULS_TAB = CREATIVE_TABS.register("qsouls_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.AZURE_NEEDLE.get()))
                    .title(Component.translatable("creativetab.qsouls_tab"))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.AZURE_NEEDLE.get());
                        output.accept(ModItems.TITAN_RIB.get());
                        output.accept(ModItems.ECHOING_STINGER.get());
                        output.accept(ModItems.GALE_HUNTER.get());
                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_TABS.register(eventBus);
    }
}