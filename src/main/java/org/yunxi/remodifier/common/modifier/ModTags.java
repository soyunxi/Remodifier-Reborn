package org.yunxi.remodifier.common.modifier;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.yunxi.remodifier.Remodifier;

public class ModTags {
    public static class Items {
        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(Remodifier.MODID, name));
        }

        public static final TagKey<Item> WEAPON_ITEMS = createTag("weapon");

        public static final TagKey<Item> ARMOR_ITEMS = createTag("armor");

        public static final TagKey<Item> BOW_ITEMS = createTag("bow");

        public static final TagKey<Item> SHIELD_ITEMS = createTag("shield");

        public static final TagKey<Item> TOOL_ITEMS = createTag("tool");

    }
}
