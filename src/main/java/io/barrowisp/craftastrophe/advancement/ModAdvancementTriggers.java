package io.barrowisp.craftastrophe.advancement;

import net.minecraft.advancements.CriteriaTriggers;

/**
 * This class handles initialization and registration of custom advancement triggers.
 */
public class ModAdvancementTriggers
{
    //public static ReadBlueprint readBlueprint;

    private static void register(ModTrigger trigger)
    {
        CriteriaTriggers.register(trigger);
    }
    /**
     *  Initialize and register all advancement triggers here.
     */
    public static void init()
    {
        //register(readBlueprint = new ReadBlueprint());
    }

}
