package io.barrowisp.craftastrophe.items;

import io.barrowisp.craftastrophe.ModLogger;
import io.barrowisp.craftastrophe.advancement.ModAdvancementTriggers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class Blueprint extends ItemBase {

    public Blueprint(String name) {
        super(name);
    }

    /**
     * Called when the equipped item is right clicked.
     */
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ModLogger.debug("Trying to read blueprint...");
        if (playerIn instanceof EntityPlayerMP) {
            /*
             * TODO: Move this test variable in a dedicate field once we create
             *       a custom player property for knowledge
             */
            int knowledge = 1;
            ModAdvancementTriggers.readBlueprint.trigger((EntityPlayerMP) playerIn, knowledge);
         }
        return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }
}
