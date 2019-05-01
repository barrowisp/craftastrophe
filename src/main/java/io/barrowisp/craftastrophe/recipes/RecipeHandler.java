package io.barrowisp.craftastrophe.recipes;

import io.barrowisp.craftastrophe.ModLogger;

// TODO: Document this class and all it's methods
public class RecipeHandler {

    private static RecipeHandler instance;
    private final CustomRecipes customRecipes;

    private RecipeHandler() {
        customRecipes = new CustomRecipes();
    }

    public static void init() {

        if (instance == null)
            instance = new RecipeHandler();
        else
            ModLogger.warn("Trying to initialize RecipeHandler more then once");
    }

    static CustomRecipes getCustomRecipes() {
        return instance.customRecipes;
    }
}
