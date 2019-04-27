package io.barrowisp.craftastrophe;

import io.barrowisp.craftastrophe.items.ModItem;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber
public class RegistryHandler {

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        Craftastrophe.LOGGER.info("Registering mod items...");
        final IForgeRegistry<Item> registry = event.getRegistry();
        for (ModItem item : ModItem.values())
            registry.register(item.get());
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event)
    {
        Craftastrophe.LOGGER.info("Registering asset models...");
        for (ModItem item : ModItem.values())
            registerModel(item.get());
    }

    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unused")
    private static void registerModel(Block block)
    {
        registerModel(Item.getItemFromBlock(block));
    }

    @SideOnly(Side.CLIENT)
    private static void registerModel(Item item)
    {
        net.minecraft.util.ResourceLocation location = java.util.Objects.requireNonNull(item.getRegistryName());
        ModelResourceLocation modelLocation = new ModelResourceLocation(location, "inventory");
        ModelLoader.setCustomModelResourceLocation(item, 0, modelLocation);
    }
}
