package io.barrowisp.craftastrophe.items;

import io.barrowisp.craftastrophe.Craftastrophe;
import net.minecraft.item.Item;

public class ItemBase extends Item {

    protected ItemBase(String name) {

        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(Craftastrophe.tabCraftastrophe);
    }
}
