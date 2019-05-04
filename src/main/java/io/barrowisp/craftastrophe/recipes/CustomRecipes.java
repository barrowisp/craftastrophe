package io.barrowisp.craftastrophe.recipes;

import io.barrowisp.craftastrophe.ModLogger;
import javafx.util.Pair;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Map;

/**
 *  This class contains custom mod recipes as well all methods
 *  related to handling these entries. The class instance is created
 *  and stored in {@link io.barrowisp.craftastrophe.recipes.RecipeHandler}.
 */
public class CustomRecipes {

    /** List of custom mod recipes paired with custom data entries */
    final Map<IRecipe, RecipeData> map;
    private final Map<ResourceLocation, IRecipe> entries;

    CustomRecipes() {
        Pair<Map<IRecipe, RecipeData>, Map<ResourceLocation, IRecipe>> data = readCustomRecipes();
        map = java.util.Collections.unmodifiableMap(data.getKey());
        entries = java.util.Collections.unmodifiableMap(data.getValue());
    }

    }

    /** Get recipe data for custom mod recipe */
    public static RecipeData getRecipeData(IRecipe recipe) {
        return RecipeHandler.getCustomRecipes().map.get(recipe);
    }

    /**
     * Returns a list of random custom recipes from an internal map
     * @param maxAmount maximum amount of recipes we want to get.
     *        <p>The actual number of recipes returned is random.</p>
     * @return a random list of custom recipes
     */
    public static java.util.List<IRecipe> getRandom(int maxAmount) {

        ModLogger.debug("Getting random number of custom recipes (1-%d)", maxAmount);
        CustomRecipes customRecipes = RecipeHandler.getCustomRecipes();
        java.util.List<IRecipe> recipes = new java.util.ArrayList<>();
        /* No custom mod recipes found in Forge registry */
        if (customRecipes.map.isEmpty()) {
            return recipes;
        }
        /* Convert the map key set to a primitive array for easier access */
        IRecipe[] recipeArray = customRecipes.map.keySet().toArray(new IRecipe[customRecipes.map.size()]);

        for (int am = new java.util.Random().nextInt(maxAmount) + 1; am > 0; am--) {
            int index = new java.util.Random().nextInt(customRecipes.map.size());
            recipes.add(recipeArray[index]);
        }
        return recipes;
    }

    /**
     * Read the game recipes from Forge registry and find custom mod recipes.
     * @return list of custom mod recipes. Returns an empty list if no custom recipes are found.
     */
    private static Pair<Map<IRecipe, RecipeData>, Map<ResourceLocation, IRecipe>> readCustomRecipes() {

        IForgeRegistry<IRecipe> vanillaRecipes = ForgeRegistries.RECIPES;
        Map<IRecipe, RecipeData> modRecipes = new java.util.Hashtable<>();
        Map<ResourceLocation, IRecipe> recipeEntries = new java.util.Hashtable<>();

        ModLogger.debug("Iterating through game recipes (size: %d)", vanillaRecipes.getEntries().size());
        for (IRecipe recipe : vanillaRecipes) {
            if (CustomRecipes.validate(recipe)) {
                modRecipes.put(recipe, new RecipeData(recipe));
                recipeEntries.put(recipe.getRegistryName(), recipe);
            }
        }
        return new Pair<>(modRecipes, recipeEntries);
    }

    /**
     * Check to see if the recipe is a custom mod recipe.
     *
     * @param recipe instance of the recipe to validate
     * @return {@code true} if the recipe is custom
     */
    private static boolean validate(IRecipe recipe) {

        String[] recipeMeta = RecipeData.getMeta(recipe);
        return recipeMeta.length > 1 && !recipeMeta[0].equals("minecraft");
    }
}
