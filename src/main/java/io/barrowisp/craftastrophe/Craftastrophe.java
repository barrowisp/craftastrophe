package io.barrowisp.craftastrophe;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = Craftastrophe.MODID, name = Craftastrophe.NAME, version = Craftastrophe.VERSION)
public class Craftastrophe {

    @Mod.Instance
    public static Craftastrophe instance;

    public static final String MODID = "craftastrophe";
    public static final String NAME = "Craftastrophe";
    public static final String VERSION = "0.1";

    private static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        logger.info("Pre-initializing " + Craftastrophe.MODID + "...");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        logger.info("Initializing " + Craftastrophe.MODID + "...");
    }
}
