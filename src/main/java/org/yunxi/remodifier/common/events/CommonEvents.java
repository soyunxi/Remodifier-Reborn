package org.yunxi.remodifier.common.events;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.yunxi.remodifier.common.attribute.Attributes;
import org.yunxi.remodifier.common.item.ModifierBookItem;
import org.yunxi.remodifier.common.modifier.Modifier;
import org.yunxi.remodifier.common.modifier.ModifierHandler;
import org.yunxi.remodifier.common.modifier.Modifiers;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class CommonEvents {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack right = event.getRight();
        if (right.getItem() instanceof ModifierBookItem && ModifierHandler.canHaveModifiers(event.getLeft()) && right.getTag() != null) {
            Modifier modifier = Modifiers.MODIFIERS.get(ResourceLocation.parse(right.getTag().getString(ModifierHandler.bookTagName)));
            if (modifier != null) {
                ItemStack output = event.getLeft().copy();
                ModifierHandler.setModifier(output, modifier);
                event.setMaterialCost(1);
                event.setCost(1);
                event.setOutput(output);
                event.setCanceled(false);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onEquipChange(LivingEquipmentChangeEvent event) {
        EquipmentSlot slotType = event.getSlot();
        ItemStack from = event.getFrom();
        ItemStack to = event.getTo();
        Modifier fromMod = ModifierHandler.getModifier(from);
        if (fromMod != null) ModifierHandler.removeEquipmentModifier(event.getEntity(), fromMod, slotType);
        Modifier toMod = ModifierHandler.getModifier(to);
        if (toMod == null) {
            toMod = ModifierHandler.rollModifier(to, ThreadLocalRandom.current());
            if (toMod == null) return;
            ModifierHandler.setModifier(to, toMod);
        }
        ModifierHandler.applyEquipmentModifier(event.getEntity(), toMod, slotType);
    }

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof AbstractArrow arrow) {
            if (arrow.getOwner() instanceof Player player) {
                ItemStack mainHandItem = player.getMainHandItem();
                CompoundTag orCreateTag = mainHandItem.getOrCreateTag();
                double random = orCreateTag.getDouble("random");
                AttributeInstance attribute = player.getAttribute(Attributes.NO_CONSUMPTION.get());
                if (attribute != null) {
                    if (attribute.getValue() >= random) {
                        arrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                        orCreateTag.remove("random");
                    }
                }
            }
        }
    }
}
