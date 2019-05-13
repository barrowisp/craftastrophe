package io.barrowisp.craftastrophe.capabilities;

import io.barrowisp.craftastrophe.CFLogger;
import io.barrowisp.craftastrophe.Craftastrophe;
import io.yooksi.commons.define.PositiveRange;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.Contract;

/**
 * <p>This capability is an arbitrary representation of players knowledge of the world.</p>
 * It should increase with reading and exploration, and should never be decreased.
 */
public interface Knowledge extends PlayerCapability {

    ResourceLocation RL = new ResourceLocation(Craftastrophe.MODID, "knowledge");
    CapabilityProvider<Knowledge> PROVIDER = new CapabilityProvider<>(ModCapabilities.KNOWLEDGE);

    static void increasePlayerKnowledge(EntityPlayer player, int amount) {
        ModCapabilities.getPlayerKnowledge(player).increase(amount);
    }
    static int getPlayerKnowledge(EntityPlayer player) {
        return ModCapabilities.getPlayerKnowledge(player).get();
    }

    @Override
    default PlayerCapability get(EntityPlayerMP player) {
        return player.getCapability(ModCapabilities.KNOWLEDGE, null);
    }

    int get();
    void increase(int amount);

    class Impl implements Knowledge {

        private static final int MAX_KNOWLEDGE = 10;

        @PositiveRange(max = MAX_KNOWLEDGE)
        private int knowledge = 0;

        @Override
        public void dataChanged(EntityPlayerMP player) {
            //NetworkHandler.network.sendTo();
        }

        @Override
        @Contract(pure = true)
        public NBTTagCompound serializeNBT() {

            CFLogger.debug("Saving player knowledge %d", knowledge);
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setInteger("knowledge", knowledge);
            return nbt;
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {

            knowledge = nbt.getInteger("knowledge");
            CFLogger.debug("Loaded player knowledge %d", knowledge);
        }

        @Override
        @Contract(pure = true)
        public int get() {
            return knowledge;
        }

        @Override
        public void increase(int amount) {

            CFLogger.debug("Increasing player knowledge(%d) for %d", knowledge, amount);
            knowledge += amount;
            if (knowledge > MAX_KNOWLEDGE) {
                knowledge = MAX_KNOWLEDGE;
            }
            else if (knowledge < 0) {
                knowledge = 0;
            }
        }
    }
}
