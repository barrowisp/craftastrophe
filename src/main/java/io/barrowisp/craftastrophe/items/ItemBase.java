package io.barrowisp.craftastrophe.items;

import io.barrowisp.craftastrophe.Craftastrophe;
import io.barrowisp.craftastrophe.defines.ForgeRegIdentifier;
import net.minecraft.item.Item;

class ItemBase extends Item {

    ItemBase(@ForgeRegIdentifier String id) {

        setUnlocalizedName(id);
        setRegistryName(id);
        setCreativeTab(Craftastrophe.tabCraftastrophe);
    }
}
