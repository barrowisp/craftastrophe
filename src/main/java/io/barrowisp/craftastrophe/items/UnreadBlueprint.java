package io.barrowisp.craftastrophe.items;

import com.mojang.realmsclient.gui.ChatFormatting;
import io.barrowisp.craftastrophe.ModLogger;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class UnreadBlueprint extends ItemBase {

    public UnreadBlueprint(String name) {
        super(name);
    }

    /**
     * Called when the player attempts to read the blueprint by right clicking in the air.<br>
     * <i>Note that this will <b>not get called</b> if the player clicks on a block.</i>
     */
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {

        if (playerIn instanceof EntityPlayerMP)
        {
            ModLogger.debug("%s is trying to read a blueprint", playerIn.getName());
            /*
             * TODO: Move this test variable in a dedicate field once we create
             *       a custom player property for knowledge
             */
            int knowledge = 3;

        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, java.util.List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(ChatFormatting.GRAY + "This paper contains some kind of crafting schematics");
    }
}
