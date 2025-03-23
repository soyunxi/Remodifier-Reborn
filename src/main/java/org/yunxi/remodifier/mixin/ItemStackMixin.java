package org.yunxi.remodifier.mixin;


import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.yunxi.remodifier.common.modifier.Modifier;
import org.yunxi.remodifier.common.modifier.ModifierHandler;
import org.yunxi.remodifier.common.modifier.Modifiers;

import javax.annotation.Nullable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow public abstract ItemStack copy();

    @Inject(method = "getHoverName", at = @At(value = "RETURN"), cancellable = true)
    private void getDisplayName(CallbackInfoReturnable<Component> cir) {
        ItemStack copy = copy();
        Modifier modifier = ModifierHandler.getModifier(copy);
        if (modifier != null && modifier != Modifiers.NONE) {
            Component formattedName = modifier.getFormattedName();
            Component displayName = copy.getItem().getName(copy);
            MutableComponent append = formattedName.copy().append(" ").append(displayName);
            cir.setReturnValue(append);
        }
    }

}
