package org.yunxi.remodifier.common.curios;

import net.minecraft.world.item.ItemStack;

public class CurioCompat implements ICurioProxy {
    @Override
    public boolean isModifiableCurio(ItemStack stack) {
        return !Cur.getCuriosHelper().getCurioTags(stack.getItem()).isEmpty();
    }
}
