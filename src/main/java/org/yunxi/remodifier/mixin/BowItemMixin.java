package org.yunxi.remodifier.mixin;


import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.yunxi.remodifier.common.attribute.Attributes;

import java.util.Random;

@Mixin(BowItem.class)
public class BowItemMixin {
    @Redirect(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ArrowItem;isInfinite(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/player/Player;)Z"))
    private boolean noConsumption(ArrowItem instance, ItemStack stack, ItemStack bow, Player player) {
        Random random = new Random();
        double v = random.nextDouble(1);
        AttributeInstance attribute = player.getAttribute(Attributes.NO_CONSUMPTION.get());
        if (attribute != null && v <= attribute.getValue()) {
            return true;
        }
        return instance.isInfinite(stack, bow, player);
    }

    @Inject(method = "getUseDuration", at = @At("HEAD"), cancellable = true)
    public void getUseDuration(ItemStack pStack, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(3);
    }
}
