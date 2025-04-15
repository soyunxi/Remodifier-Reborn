package org.yunxi.remodifier.common.modifier;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;

@SuppressWarnings("removal")
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

    public Modifier roll(ItemStack itemStack, Random random) {
        if (totalWeight == 0 || modifiers.isEmpty()) return null;
        int i = random.nextInt(totalWeight);
        int j = 0;
        for (Modifier modifier : modifiers) {
            j += modifier.weight;
            if (i < j && canAdd(itemStack, modifier)) {
                return modifier;
            }
        }
        // This shouldn't happen
        return null;
    }

    public void remove(Modifier mod) {
        modifiers.remove(mod);
        totalWeight -= mod.weight;
    }

    public void clear() {
        modifiers.clear();
        totalWeight = 0;
    }

    private boolean canAdd(ItemStack itemStack, Modifier modifier) {
        List<String> whitelist = modifier.whitelist;
        List<String> blacklist = modifier.blacklist;
        if (whitelist != null && !whitelist.isEmpty()) {
            for (String white : whitelist) {
                return isInList(itemStack, white);
            }
        } else if (blacklist != null && !blacklist.isEmpty()) {
            for (String black : blacklist){
                return !isInList(itemStack, black);
            }
        }
        return true;
    }

    private boolean isInList(ItemStack itemStack, String list) {
        if (list.startsWith("#")) {
            String substring = list.substring(1);
            TagKey<Item> tagKey = ItemTags.create(new ResourceLocation(substring));
            return itemStack.is(tagKey);
        } else if (list.startsWith("@")){
            return Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(itemStack.getItem())).getNamespace().equals(list.substring(1));
        } else {
            return Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(itemStack.getItem())).toString().equals(list);
        }
    }
}
