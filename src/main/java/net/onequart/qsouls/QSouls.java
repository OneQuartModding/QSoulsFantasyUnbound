package net.onequart.qsouls;

import com.mojang.logging.LogUtils;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.onequart.qsouls.config.QSoulsClientConfig;
import net.onequart.qsouls.network.ModMessages;
import org.slf4j.Logger;

@Mod(QSouls.MOD_ID)
public class QSouls {
    public static final String MOD_ID = "qsouls";
    public static final Logger LOGGER = LogUtils.getLogger();

    public QSouls(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, QSoulsClientConfig.SPEC);

        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory((mc, screen) -> new net.onequart.qsouls.client.gui.QSoulsConfigScreen(screen)));

        MinecraftForge.EVENT_BUS.register(this);
        ModMessages.register();
    }
}