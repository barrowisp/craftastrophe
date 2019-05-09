package io.barrowisp.craftastrophe.items;

import io.barrowisp.craftastrophe.Craftastrophe;
import net.minecraft.item.Item;

class ItemBase extends Item {

    ItemBase(String id) {

        setUnlocalizedName(id);
        setRegistryName(id);
        setCreativeTab(Craftastrophe.tabCraftastrophe);
    }
}
