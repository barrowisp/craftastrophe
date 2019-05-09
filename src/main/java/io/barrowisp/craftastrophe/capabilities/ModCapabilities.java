package io.barrowisp.craftastrophe.capabilities;

import io.barrowisp.craftastrophe.ModLogger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import java.util.concurrent.Callable;

public class ModCapabilities {
    /*
     *  Mod capability instances will be properly initialized by Forge later on.
     *  These fileds should not be changed so declare them as package
     *  private to prevent outside tampering
     */
    @CapabilityInject(Knowledge.class)
    static Capability<Knowledge> KNOWLEDGE = null;

    /**
     * Call this during mod initialization to register all mod capabilities.
     */
    public static void init() {

        ModLogger.debug("Initializing mod capabilities.");
    }
    private static <T extends PlayerCapability> void register(Class<T> type, Callable<? extends T> factory) {
        CapabilityManager.INSTANCE.register(type, new CapabilityStorage<>(), factory);
    }

}
