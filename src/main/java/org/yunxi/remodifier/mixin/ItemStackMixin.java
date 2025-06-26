package org.yunxi.remodifier.mixin;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.yunxi.remodifier.common.modifier.Modifier;
import org.yunxi.remodifier.common.modifier.ModifierHandler;
import org.yunxi.remodifier.common.modifier.Modifiers;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow public abstract boolean hasCustomHoverName();

    @Inject(method = "getHoverName", at = @At(value = "RETURN"), cancellable = true)
    private void getDisplayName(CallbackInfoReturnable<Component> cir) {
        ItemStack stack = (ItemStack) (Object) this;

        if (this.hasCustomHoverName()) {
            return;
        }

        Modifier modifier = ModifierHandler.getModifier(stack);
        if (modifier != null && modifier != Modifiers.NONE) {
            Component formattedName = modifier.getFormattedName();
            Component displayName = stack.getItem().getName(stack);
            MutableComponent newName = formattedName.copy().append(" ").append(displayName);
            cir.setReturnValue(newName);
            cir.cancel();
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen instanceof AnvilScreen) {
            Component originalName = cir.getReturnValue();
            cir.setReturnValue(originalName);
            cir.cancel();
        }
    }

}
