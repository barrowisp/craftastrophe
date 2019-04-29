package io.barrowisp.craftastrophe.advancement.trigger;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import io.barrowisp.craftastrophe.ModLogger;
import io.barrowisp.craftastrophe.advancement.ModCriterionInstance;
import io.barrowisp.craftastrophe.advancement.ModTrigger;
import net.minecraft.util.ResourceLocation;

/**
 * Custom advancement trigger that fires when we read a blueprint.
 */
public class ReadBlueprint extends ModTrigger<ReadBlueprint.Instance, Integer>
{
    public ReadBlueprint()
    {
        super("read_blueprint", "knowledge");
    }

    @Override
    public Instance deserializeInstance(JsonObject json, JsonDeserializationContext context)
    {
        String string = getTriggerConditionFrom(json);
        if(string != null)
        {
            try
            {
                return new Instance(ID, Integer.valueOf(string));
            }
            catch(NumberFormatException e)
            {
                ModLogger.error("Value " + string + " is not an Integer!", e);
            }
        }
        return new Instance(ID, Integer.MAX_VALUE);
    }

    /**
     * Attempt to read the blueprint and recieve advancement reward.
     * @param player instance of the player reading the blueprint
     * @param knowledge arbitrary value of knowledge the player possessses
     */
    @Override
    public void trigger(EntityPlayerMP player, Integer knowledge)
    {
        ModLogger.debug("Figuring out schematics with knowledge " + knowledge);
        super.trigger(player, knowledge);
    }

    /**
     *  Instance of our advancement critrion that holds
     *  values loaded from advancement JSON file.
     */
    public static class Instance extends ModCriterionInstance<Integer>
    {
        private final Integer threshold;

        public Instance(ResourceLocation id, Integer object)
        {
            super(id, object);
            threshold = object;
            ModLogger.debug("Initializing criterion instance: (threshold: " + threshold + ")");
        }

        @Override
        public boolean test(Integer object)
        {
            return object != null && object >= threshold;
        }
    }
}
