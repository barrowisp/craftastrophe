package io.barrowisp.craftastrophe;

import io.barrowisp.craftastrophe.capabilities.ModCapabilities;
import io.barrowisp.craftastrophe.items.ModItem;
import io.barrowisp.craftastrophe.recipes.RecipeHandler;
import io.yooksi.commons.aop.AOPProxy;
import io.yooksi.forgelib.ForgeMod;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@SuppressWarnings("unused")
@Mod(modid = Craftastrophe.MOD_ID, name = Craftastrophe.NAME, version = Craftastrophe.VERSION)
public class Craftastrophe extends ForgeMod {

    private static ForgeMod instance;

    public static final String MOD_ID = "craftastrophe";
    public static final String NAME = "Craftastrophe";
    public static final String VERSION = "0.1";

    public Craftastrophe() {
        super("tabCraftastrophe", net.minecraft.init.Items.WRITABLE_BOOK);
        instance = AOPProxy.createFor(this);
    }

    public @NotEmpty String getModId() {
        return MOD_ID;
    }
    /**
     * @return a singleton instance of this mod
     */
    public static @NotNull ForgeMod get() {
        return instance;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        /* Make sure we initialize the logging system before doing
         * anything else that might produce logs
         */
        CFLogger.init();

        CFLogger.info("Pre-initializing " + NAME + " mod...");

        ModItem.init();
//        ModAdvancementTriggers.init();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        CFLogger.info("Initializing " + NAME + " mod...");

        ModCapabilities.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

        CFLogger.info("Post-initializing " + NAME + " mod...");

        RecipeHandler.init();
    }
}
