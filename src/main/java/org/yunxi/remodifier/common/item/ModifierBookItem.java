package org.yunxi.remodifier.common.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class ModifierBookItem extends Item {
    public ModifierBookItem() {
        super(new Item.Properties().rarity(Rarity.EPIC));
    }
}
