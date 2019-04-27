package io.barrowisp.craftastrophe.advancement;

import io.barrowisp.craftastrophe.advancement.trigger.ReadBlueprint;
import net.minecraft.advancements.CriteriaTriggers;

/**
 * Created by Mark on 07/03/2018.
 */
public class ModAdvancementTriggers
{
    public static ReadBlueprint readBlueprint;

    private static void register(ModTrigger trigger)
    {
        CriteriaTriggers.register(trigger);
    }
    /**
     *  Initialize and register all advancement triggers here.
     */
    public static void init()
    {
        register(readBlueprint = new ReadBlueprint());
    }

}
