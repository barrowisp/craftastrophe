package io.barrowisp.craftastrophe.items;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.world.World;
import javax.annotation.Nullable;
public class Blueprint extends ItemBase {

    public Blueprint(String name) {
        super(name);
    }


    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, java.util.List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(ChatFormatting.GRAY + "This paper contains instructions on how to craft simple items.");
    }
}
