package io.barrowisp.craftastrophe.advancement;

import io.barrowisp.craftastrophe.CFLogger;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Mark on 07/03/2018.
 */
public class ModListeners<T extends ModCriterionInstance<O>, O>
{
    private final PlayerAdvancements advancements;
    private final Set<ICriterionTrigger.Listener<T>> listeners = new HashSet<>();

    public ModListeners(PlayerAdvancements advancements)
    {
        this.advancements = advancements;
    }

    public boolean isEmpty()
    {
        return listeners.isEmpty();
    }

    public void add(ICriterionTrigger.Listener<T> listener)
    {
        listeners.add(listener);
    }

    public void remove(ICriterionTrigger.Listener<T> listener)
    {
        listeners.remove(listener);
    }

    public void trigger(O object)
    {
        List<ICriterionTrigger.Listener<T>> list = new ArrayList<>();
        for(ICriterionTrigger.Listener<T> listener : listeners)
            if (listener.getCriterionInstance().test(object))
                list.add(listener);

        if(!list.isEmpty()) {
            for (ICriterionTrigger.Listener<T> listener : list)
            {
                CFLogger.debug("Granting criterion to player!");
                listener.grantCriterion(advancements);
            }
        }
        else CFLogger.debug("Unable to grant criterion to player.");
    }
}
