package io.barrowisp.craftastrophe;

import io.barrowisp.craftastrophe.advancement.ModAdvancementTriggers;
import io.barrowisp.craftastrophe.items.ModItem;
import io.barrowisp.craftastrophe.recipes.RecipeHandler;
import net.minecraft.init.Items;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Craftastrophe.MODID, name = Craftastrophe.NAME, version = Craftastrophe.VERSION)
public class Craftastrophe {

    @Mod.Instance
    public static Craftastrophe instance;

    public static final String MODID = "craftastrophe";
    public static final String NAME = "Craftastrophe";
    public static final String VERSION = "0.1";

    /**
     *  Create a new creative tab to be displayed in-game for our mod.
     *  By overriding {@code getTabIconItem} we are able to return our own
     *  item stack which the game will use to show a tab picture.
     */
    public static final net.minecraft.creativetab.CreativeTabs tabCraftastrophe =
            (new net.minecraft.creativetab.CreativeTabs("tabCraftastrophe") {

        @Override
        public net.minecraft.item.ItemStack getTabIconItem() {
            return new net.minecraft.item.ItemStack(Items.WRITABLE_BOOK);
        }
    });

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        /* Make sure we initialize the logging system before doing
         * anything else that might produce logs
         */
        ModLogger.init();

        ModItem.init();
//        ModAdvancementTriggers.init();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) throws ClassNotFoundException
    {
        ModLogger.info("Initializing " + Craftastrophe.MODID + "...");
        RecipeHandler.init();
    }
}
