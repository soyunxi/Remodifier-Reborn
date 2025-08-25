package org.yunxi.remodifier.mixin;

import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.yunxi.remodifier.common.attribute.Attributes;

import java.util.Random;

@Mixin(BowItem.class)
public class BowItemMixin {
    /*@Inject(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V",shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILSOFT)
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft, CallbackInfo ci, Player player, boolean flag, ItemStack itemstack, int i, float f, boolean flag1) {
        AttributeInstance attribute = player.getAttribute(Attributes.NO_CONSUMPTION.get());
        if (attribute != null) {
            double remodifier_reborn$d = new Random().nextDouble(1);
            if (attribute.getValue() >= remodifier_reborn$d) {
                itemstack.grow(1);
                CompoundTag orCreateTag = pStack.getOrCreateTag();
                orCreateTag.putDouble("random", remodifier_reborn$d);
            }
        }
    }*/

    @Redirect(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ArrowItem;isInfinite(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/player/Player;)Z"))
    public boolean releaseUsing(ArrowItem instance, ItemStack stack, ItemStack bow, Player player) {
        AttributeInstance attribute = player.getAttribute(Attributes.NO_CONSUMPTION.get());
        if (attribute != null) {
            double remodifier_reborn$d = new Random().nextDouble(1);
            if (attribute.getValue() >= remodifier_reborn$d) {
                return true;
            }
        }
        return false;
    }

}

