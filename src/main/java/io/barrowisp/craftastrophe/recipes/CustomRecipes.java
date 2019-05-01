package io.barrowisp.craftastrophe.recipes;

import io.barrowisp.craftastrophe.ModLogger;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

/**
 *  This class contains custom mod recipes as well all methods
 *  related to handling these entries. The class instance is created
 *  and stored in {@link io.barrowisp.craftastrophe.recipes.RecipeHandler}.
 */
public class CustomRecipes {

    /** List of custom mod recipes paired with custom data entries */
    private final java.util.Map<IRecipe, RecipeData> map;

    CustomRecipes() {
        map = java.util.Collections.unmodifiableMap(readCustomRecipes());
    }

    /** Get recipe data for custom mod recipe */
    public static RecipeData getRecipeData(IRecipe recipe) {
        return RecipeHandler.getCustomRecipes().map.get(recipe);
    }

    /**
     * Returns a list of random custom recipes
     * @param amount maximum amount of recipes we want to get.
     *               The actual number of recipes returned is random.
     * @return a random list of custom recipes
     */
    public static java.util.List<IRecipe> getRandom(int amount) {

        CustomRecipes customRecipes = RecipeHandler.getCustomRecipes();
        java.util.List<IRecipe> recipes = new java.util.ArrayList<>();
        /* No custom mod recipes found in Forge registry */
        if (customRecipes.map.size() == 0)
            return recipes;

        for (int am = new java.util.Random().nextInt(amount - 1) + 1; am > 0; am--) {
            int index = new java.util.Random().nextInt(customRecipes.map.size());
            recipes.add((IRecipe) customRecipes.map.keySet().toArray()[index]);
        }
        return recipes;
    }

    /**
     * Read the game recipes from Forge registry and find custom mod recipes.
     * @return list of custom mod recipes. Returns an empty list if no custom recipes are found.
     */
    private static java.util.Map<IRecipe, RecipeData> readCustomRecipes() {

        IForgeRegistry<IRecipe> vanillaRecipes = ForgeRegistries.RECIPES;
        java.util.Map<IRecipe, RecipeData> modRecipes = new java.util.HashMap<>();

        ModLogger.debug("Iterating through game recipes (size: %d)", vanillaRecipes.getEntries().size());
        for (IRecipe recipe : vanillaRecipes) {
            if (CustomRecipes.validate(recipe)) {
                modRecipes.put(recipe, new RecipeData(recipe));
            }
        }
        return modRecipes;
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
