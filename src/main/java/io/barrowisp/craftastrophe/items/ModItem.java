package io.barrowisp.craftastrophe.items;

import io.barrowisp.craftastrophe.CFLogger;
import io.barrowisp.craftastrophe.Craftastrophe;
import io.yooksi.forgelib.ItemBase;
import net.minecraft.item.Item;
import org.jetbrains.annotations.Contract;

public enum ModItem {

    BLUEPRINT(Blueprint.class, "blueprint"),

    /**
     * Attempting to add another custom Item,
     * as an additional 'constant' in the enum ModItem,
     * so changed the semicolon ; at the end of the BLUEPRINT constant to a comma ,
     */
    BINDER(Binder.class, "binder");

    private static java.util.List<Item> ITEMS = new java.util.ArrayList<>();
    private final ItemBase instance;

    ModItem(Class<? extends ItemBase> itemClass, String name) {
        instance = ItemBase.construct(itemClass, name, Craftastrophe.get());
    }

    @Contract(pure = true)
    public Item get() {
        return instance;
    }

    public boolean isItemIn(net.minecraft.item.ItemStack stack) {
        return instance.getClass() == stack.getItem().getClass();
    }

    /**
     * Initialize all mod items and store them in a list.
     */
    public static void init() {

        CFLogger.debug("Initializing mod items...");
        for (ModItem i : ModItem.values()) ModItem.ITEMS.add(i.instance);
        ModItem.ITEMS = java.util.Collections.unmodifiableList(ModItem.ITEMS);
    }
}
