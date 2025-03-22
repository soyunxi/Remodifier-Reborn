package org.yunxi.remodifier.common.modifier;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.world.item.ItemStack;

import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;

public class ModifierPool {
    public Predicate<ItemStack> isApplicable;
    public int totalWeight = 0;

    public Set<Modifier> modifiers = new ObjectOpenHashSet<>();

    public ModifierPool(Predicate<ItemStack> isApplicable) {
        this.isApplicable = isApplicable;
    }

    public void add(Modifier mod) {
        modifiers.add(mod);
        totalWeight += mod.weight;
    }

    public Modifier roll(Random random) {
        if (totalWeight == 0 || modifiers.isEmpty()) return null;
        int i = random.nextInt(totalWeight);
        int j = 0;
        for (Modifier modifier : modifiers) {
            j += modifier.weight;
            if (i < j) {
                return modifier;
            }
        }
        // This shouldn't happen
        return null;
    }
}
