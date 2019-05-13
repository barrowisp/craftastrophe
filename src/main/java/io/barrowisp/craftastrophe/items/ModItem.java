package io.barrowisp.craftastrophe.items;

import io.barrowisp.craftastrophe.CFLogger;
import io.yooksi.commons.validator.BeanValidator;
import net.minecraft.item.Item;
import org.jetbrains.annotations.Contract;

public enum ModItem {

    BLUEPRINT(Blueprint.class, "craftastrophe:blueprint"),

    /**
     * Attempting to add another custom Item,
     * as an additional 'constant' in the enum ModItem,
     * so changed the semicolon ; at the end of the BLUEPRINT constant to a comma ,
     */
    BINDER(Binder.class, "craftastrophe:binder");

    private static java.util.List<Item> ITEMS = new java.util.ArrayList<>();
    private final ItemBase instance;

    ModItem(Class<? extends ItemBase> itemClass, String id) {
        instance = BeanValidator.constructChild(ItemBase.class, itemClass, id);
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
