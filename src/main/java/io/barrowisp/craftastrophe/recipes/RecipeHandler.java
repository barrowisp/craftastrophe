package io.barrowisp.craftastrophe.recipes;

import io.barrowisp.craftastrophe.Craftastrophe;
import io.yooksi.forgelib.ModLogger;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public abstract class RecipeHandler {

    /**
     * Call this from {@link Craftastrophe#init(FMLInitializationEvent)} after all other
     * mod recipes are already initialized in Forge recipe registry.
     * @throws ClassNotFoundException if the {@code CustomRecipes} class was not found
     */
    public static void init() throws ClassNotFoundException {
        ModLogger.debug("Loading CustomRecipes class");
        Class.forName(CustomRecipes.class.getName());
    }
}
