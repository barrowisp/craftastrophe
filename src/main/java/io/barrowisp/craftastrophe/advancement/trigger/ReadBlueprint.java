package io.barrowisp.craftastrophe.advancement.trigger;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import io.barrowisp.craftastrophe.ModLogger;
import io.barrowisp.craftastrophe.advancement.ModCriterionInstance;
import io.barrowisp.craftastrophe.advancement.ModTrigger;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Mark on 07/03/2018.
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
        String string = getObjectStringFromJson(json);
        if(string != null)
        {
            int value;
            try
            {
                value = Integer.valueOf(string);
                return new Instance(ID, value);
            }
            catch(NumberFormatException e)
            {
                ModLogger.get().error("Value " + string + " is not a float!", e);
            }
        }
        return new Instance(ID, Integer.MAX_VALUE);
    }

    public static class Instance extends ModCriterionInstance<Integer>
    {
        private final Integer object;

        public Instance(ResourceLocation id, Integer object)
        {
            super(id, object);
            this.object = object;
        }

        @Override
        public boolean test(Integer object)
        {
            return object != null && object < this.object;
        }
    }
}
