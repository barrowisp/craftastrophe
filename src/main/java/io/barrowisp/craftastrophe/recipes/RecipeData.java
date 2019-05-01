package io.barrowisp.craftastrophe.recipes;

import io.barrowisp.craftastrophe.ModLogger;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 *  Data class for custom mod recipes that holds resource location information.
 */
public class RecipeData  {

    private final String modId;
    private final String name;

    RecipeData(IRecipe recipe) {

        String[] recipeMeta = getMeta(recipe);
        modId = recipeMeta[0]; name = recipeMeta[1];
        ModLogger.debug("Reading custom recipe %s from mod %s", name, modId);
    }

    /**
     * Get the name of the mod that created this recipe.
     * This is useful if we want to display it in blueprint tooltips.
     * @return mod name as it displays in the game
     */
    public String getModName() {
        return FMLCommonHandler.instance().findContainerFor(modId).getName();
    }

    /**
     * Split the recipe resource location into a string array
     * @param recipe instance of the recipe to get meta for
     * @return mod ID and recipe name in string array
     */
    @NotNull
    static String[] getMeta(IRecipe recipe) {
        return Objects.requireNonNull(recipe.getRegistryName()).toString().split(":");
    }
}
