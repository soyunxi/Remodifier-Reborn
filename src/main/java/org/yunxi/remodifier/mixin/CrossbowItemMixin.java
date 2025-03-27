package org.yunxi.remodifier.mixin;


import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.yunxi.remodifier.common.attribute.Attributes;

import java.util.Random;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin {

    @Inject(method = "loadProjectile", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;split(I)Lnet/minecraft/world/item/ItemStack;"))
    private static void noConsumption(LivingEntity pShooter, ItemStack pCrossbowStack, ItemStack pAmmoStack, boolean pHasAmmo, boolean pIsCreative, CallbackInfoReturnable<Boolean> cir) {
        if (pShooter instanceof Player player) {
            AttributeInstance attribute = player.getAttribute(Attributes.NO_CONSUMPTION.get());
            if (attribute != null) {
                double remodifier_reborn$d = new Random().nextDouble(1);
                if (attribute.getValue() >= remodifier_reborn$d) {
                    pAmmoStack.grow(1);
                    CompoundTag orCreateTag = pCrossbowStack.getOrCreateTag();
                    orCreateTag.putDouble("random", remodifier_reborn$d);
                }
            }
        }
    }
}
