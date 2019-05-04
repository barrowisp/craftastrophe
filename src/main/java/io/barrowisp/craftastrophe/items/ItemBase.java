package io.barrowisp.craftastrophe.items;

import io.barrowisp.craftastrophe.Craftastrophe;
import io.barrowisp.craftastrophe.defines.ModItemName;
import net.minecraft.item.Item;

class ItemBase extends Item {

    ItemBase(@ModItemName String name) {

        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(Craftastrophe.tabCraftastrophe);
    }
}
