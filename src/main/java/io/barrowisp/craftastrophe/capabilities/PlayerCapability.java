package io.barrowisp.craftastrophe.capabilities;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public interface PlayerCapability extends INBTSerializable<NBTTagCompound> {

    /** Will send updates for all data in this capability */
    void dataChanged(EntityPlayerMP player);

    /** Use this to get capability instances for different players */
    PlayerCapability get(EntityPlayerMP player);

    /** Copy all capability data to new player */
    default void copyDataTo(EntityPlayerMP playerNew)
    {
        PlayerCapability updatedCapability = get(playerNew);
        updatedCapability.deserializeNBT(serializeNBT());
        updatedCapability.dataChanged(playerNew);
    }
}
