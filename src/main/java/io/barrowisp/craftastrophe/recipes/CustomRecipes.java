package io.barrowisp.craftastrophe.recipes;

import io.barrowisp.craftastrophe.CFLogger;
import io.yooksi.commons.define.MethodsNotNull;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.Nullable;

import javax.annotation.concurrent.Immutable;
import java.util.Map;

/**
 * This class contains mappings for custom mod recipes as well all methods
 * related to handling these entries. The mappings are initialized on first class load.
 * @see RecipeHandler#init()
 */
@Immutable
@MethodsNotNull
public abstract class CustomRecipes {

    /**
     * Custom mod recipes found in Forge registry mapped to their registry names
     * so that they can be easily retrieved by systems like blueprints and others
     * that store recipe references in itemstack {@code NBTTagLists}.
     */
    private static final Map<ResourceLocation, IRecipe> map =
            java.util.Collections.unmodifiableMap(readCustomRecipes());

    /** @return {@code false} if no custom mod recipes were found in Forge registry */
    public static boolean exist() {
        return !map.isEmpty();
    }
    /** @return custom {@code IRecipe} that corresponds to parameter {@code ResourceLocation} */
    public static @Nullable IRecipe getRecipe(ResourceLocation location) {
        return map.get(location);
    }
    /**
     * @return array of recipe output item names useful for tooltips
     */
    public static String[] getOutputs(java.util.List<IRecipe> recipes) {

        java.util.Set<String> outputs = new java.util.HashSet<>();
        for (IRecipe recipe : recipes) {
            outputs.add(recipe.getRecipeOutput().getDisplayName());
        }
        return outputs.toArray(new String[0]);
    }
    /**
     * Returns a list of random custom recipes from an internal map
     * @param maxAmount maximum amount of recipes we want to get.
     *        <p>The actual number of recipes returned is random.</p>
     * @return a random list of custom recipes
     */
    public static java.util.List<IRecipe> getRandom(int maxAmount) {

        CFLogger.debug("Getting random number of custom recipes (1-%d)", maxAmount);
        java.util.List<IRecipe> recipes = new java.util.ArrayList<>();
        /* No custom mod recipes found in Forge registry */
        if (map.isEmpty()) return recipes;

        /* Convert the map key set to a primitive array for easier access */
        IRecipe[] recipeArray = map.values().toArray(new IRecipe[0]);

        for (int am = new java.util.Random().nextInt(maxAmount) + 1; am > 0; am--) {
            int index = new java.util.Random().nextInt(map.size());
            recipes.add(recipeArray[index]);
        }
        return recipes;
    }

    /**
     * Iterate through Forge recipe registry and return other mod recipes
     */
    private static Map<ResourceLocation, IRecipe> readCustomRecipes() {

        IForgeRegistry<IRecipe> gameRecipes = ForgeRegistries.RECIPES;
        Map<ResourceLocation, IRecipe> modRecipes = new java.util.Hashtable<>();

        CFLogger.debug("Iterating through game recipes (size: %d)", gameRecipes.getEntries().size());
        for (IRecipe recipe : gameRecipes)
        {
            ResourceLocation location = recipe.getRegistryName();
            String domain = location != null ? location.getNamespace() : "";

            if (!domain.isEmpty() && !domain.equals("minecraft")) {
                modRecipes.put(location, recipe);
            }
        }
        CFLogger.debug("Found %d custom mod recipes", modRecipes.size());
        return modRecipes;
    }
}
