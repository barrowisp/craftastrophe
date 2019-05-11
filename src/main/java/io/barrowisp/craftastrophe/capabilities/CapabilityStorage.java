package io.barrowisp.craftastrophe.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

/**
 * This class interacts with {@link CapabilityProvider} to write and read from NBT.
 * It is instantiated in every new mod capability registration process.
 *
 * @param <T> PlayerCapability interface this storage belongs to
 */
public class CapabilityStorage<T extends PlayerCapability> implements Capability.IStorage<T> {

    @Override @Nullable
    public NBTBase writeNBT(Capability<T> capability, T instance, EnumFacing side) {
        return instance.serializeNBT();
    }

    @Override
    public void readNBT(Capability<T> capability, T instance, EnumFacing side, NBTBase nbt) {
        instance.deserializeNBT((NBTTagCompound) nbt);
    }
}
