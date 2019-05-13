package io.barrowisp.craftastrophe.recipes;

import io.barrowisp.craftastrophe.CFLogger;
import io.barrowisp.craftastrophe.Craftastrophe;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

public abstract class RecipeHandler {

    /**
     * Call this from {@link Craftastrophe#postInit(FMLPostInitializationEvent)} after all other
     * mod recipes are already initialized in Forge recipe registry.
     * @throws IllegalStateException if the {@code CustomRecipes} class was not found
     */
    public static void init() {
        CFLogger.debug("Loading CustomRecipes class");
        try {
            Class.forName(CustomRecipes.class.getName());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }
}
