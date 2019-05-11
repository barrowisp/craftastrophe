package io.barrowisp.craftastrophe.capabilities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This class stores the capability implementation instance and
 * provides override methods that trigger NBT serialization operations.
 *
 * @param <C> PlayerCapability interface this provider belongs to
 */
@Mod.EventBusSubscriber
public class CapabilityProvider<C extends PlayerCapability> implements ICapabilitySerializable<NBTTagCompound> {

    private final Capability<C> capability;
    private final C instance;

    CapabilityProvider(Capability<C> capability)
    {
        this.capability = capability;
        instance = capability.getDefaultInstance();
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return this.capability == capability;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return hasCapability(capability, facing) ? (T) instance : null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return instance.serializeNBT();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        instance.deserializeNBT(nbt);
    }
}
