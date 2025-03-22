package org.yunxi.remodifier.common.curios;

import net.minecraft.world.item.ItemStack;

public interface ICurioProxy {
    default boolean isModifiableCurio(ItemStack stack) {
        return false;
    }
}
