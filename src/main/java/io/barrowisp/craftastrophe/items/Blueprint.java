package io.barrowisp.craftastrophe.items;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Collections;

public class Blueprint extends ItemBase {

    static final int MAX_CUSTOM_RECIPES = 3;
    static final java.util.Map<ItemStack, java.util.List<IRecipe>>
            recipeRegistry = new java.util.HashMap<>();

    public Blueprint(String name) {
        super(name);
    }

    static void registerBlueprint(ItemStack stack, java.util.List<IRecipe> recipes) {
        recipeRegistry.put(stack, Collections.unmodifiableList(recipes));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, java.util.List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(ChatFormatting.GRAY + "This paper contains instructions on how to craft simple items.");
    }
}
