package io.barrowisp.craftastrophe.advancement;

import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Mark on 07/03/2018.
 */
public abstract class ModCriterionInstance<T> extends AbstractCriterionInstance
{
    protected final T object;

    public ModCriterionInstance(ResourceLocation id, T object)
    {
        super(id);
        this.object = object;
    }
    /**
     * Test to see if the criterion should be granted
     * @param object instance of the object used for validating
     * @return {@code true} if the criterion should be granted
     */
    public abstract boolean test(T object);
}
