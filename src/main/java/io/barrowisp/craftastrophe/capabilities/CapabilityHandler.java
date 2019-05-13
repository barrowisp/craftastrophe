package io.barrowisp.craftastrophe.capabilities;

import io.barrowisp.craftastrophe.CFLogger;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
@SuppressWarnings("unused")
public class CapabilityHandler {

    /**
     * Fired whenever an object with Capabilities support is created.
     * Allowing for the attachment of arbitrary capability providers.
     * This is where we will attach custom capabilities to players.
     */
    @SubscribeEvent
    public static void attachCapability(AttachCapabilitiesEvent<Entity> event)
    {
        if(event.getObject() instanceof EntityPlayer)
        {
            CFLogger.debug("Attaching capability to EntityPlayer.");
            event.addCapability(Knowledge.RL, Knowledge.PROVIDER);
        }
    }

//    @SubscribeEvent
//    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
//    {
//        /*  Updates the client capabilities when they log into the server */
//        if(event.player instanceof EntityPlayerMP)
//        {
//            /*EntityPlayerMP player = (EntityPlayerMP) event.player;
//            Knowledge cap = player.getCapability(ModCapabilities.KNOWLEDGE, null);
//            if (cap != null)
//                cap.dataChanged(player);*/
//        }
//    }

    @SubscribeEvent
    public static void onClonePlayer(net.minecraftforge.event.entity.player.PlayerEvent.Clone event)
    {
        /*  Copy capability data over to the new player entity on death
         */
        if(event.isWasDeath() && event.getEntityPlayer() instanceof EntityPlayerMP)
        {
            CFLogger.debug("Cloning %s after player death.)", event.getEntityPlayer().getName());

            EntityPlayer playerOld = event.getOriginal();
            EntityPlayerMP playerNew = (EntityPlayerMP) event.getEntityPlayer();
            ModCapabilities.getPlayerKnowledge(playerOld).copyDataTo(playerNew);
        }
    }
}
