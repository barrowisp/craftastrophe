package io.barrowisp.craftastrophe.items;

import io.barrowisp.craftastrophe.ModLogger;
import net.minecraft.item.Item;
import org.jetbrains.annotations.Contract;

public enum ModItem {

    BLUEPRINT(new Blueprint("blueprint")),

    /**
     * Attempting to add another custom Item,
     * as an additional 'constant' in the enum ModItem,
     * so changed the semicolon ; at the end of the BLUEPRINT constant to a comma ,
     */
    BINDER(new Binder("binder"));

    private static java.util.List<Item> ITEMS = new java.util.ArrayList<>();
    private final ItemBase instance;

    ModItem(ItemBase item) {
        instance = item;
    }
    @Contract(pure = true)
    public Item get() {
        return instance;
    }
    /**
     * Initialize all mod items and store them in a list.
     */
    public static void init() {
        ModLogger.debug("Initializing mod items...");
        for (ModItem i : ModItem.values()) ModItem.ITEMS.add(i.instance);
        ModItem.ITEMS = java.util.Collections.unmodifiableList(ModItem.ITEMS);
    }
}
