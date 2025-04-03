package org.yunxi.remodifier.common.events;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.yunxi.remodifier.common.attribute.AttributeHandler;
import org.yunxi.remodifier.common.attribute.Attributes;
import org.yunxi.remodifier.common.item.ModifierBookItem;
import org.yunxi.remodifier.common.modifier.Modifier;
import org.yunxi.remodifier.common.modifier.ModifierHandler;
import org.yunxi.remodifier.common.modifier.Modifiers;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("removal")
public class CommonEvents {
    public static final Random random = new Random();

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack right = event.getRight();
        if (right.getItem() instanceof ModifierBookItem && ModifierHandler.canHaveModifiers(event.getLeft()) && right.getTag() != null) {
            Modifier modifier = Modifiers.MODIFIERS.get(new ResourceLocation(right.getTag().getString(ModifierHandler.bookTagName)));
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
                if (mainHandItem.getItem() instanceof ProjectileWeaponItem) {
                    arrow.setDeltaMovement(arrow.getDeltaMovement().scale(player.getAttribute(Attributes.BULLET_SPEED.get()).getValue()));
                    arrow.setBaseDamage(arrow.getBaseDamage() * player.getAttribute(Attributes.BULLET_DAMAGE.get()).getValue());
                }
                if (mainHandItem.getItem() instanceof CrossbowItem) {
                    CompoundTag orCreateTag = mainHandItem.getOrCreateTag();
                    if (orCreateTag.contains("random")) {
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
    }

    @SubscribeEvent
    public static void onLivingEntityUseItemTick(LivingEntityUseItemEvent.Tick event) {
        if (event.getEntity() instanceof Player player) {
            AttributeInstance attribute = player.getAttribute(Attributes.POWER_SPEED.get());
            if (attribute.getValue() != 1 ||event.getItem().getItem() instanceof ProjectileWeaponItem) {
                double temp = attribute.getValue() - 1;
                int offset = -1;
                if (temp < (double)0.0F) {
                    offset = 1;
                    temp = -temp;
                }

                while(temp > (double)1.0F) {
                    event.setDuration(event.getDuration() + offset);
                    --temp;
                }

                if (temp > (double)0.5F) {
                    if (event.getEntity().tickCount % 2 == 0) {
                        event.setDuration(event.getDuration() + offset);
                    }

                    temp -= (double)0.5F;
                }

                int mod = (int)Math.floor((double)1.0F / Math.min((double)1.0F, temp));
                if (event.getEntity().tickCount % mod == 0) {
                    event.setDuration(event.getDuration() + offset);
                }

                --temp;
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingDamage(LivingDamageEvent event) {
        Entity entity = event.getSource().getEntity();
        if (entity instanceof Player player) {
            double criticalDamageCoefficients = AttributeHandler.getCriticalDamageCoefficients(player);
            AttributeInstance attribute = player.getAttribute(Attributes.CRITICAL_HIT.get());
            double v = random.nextDouble(1);
            if (v <= attribute.getValue()) {
                event.setAmount((float) (event.getAmount() * criticalDamageCoefficients));
            }
            AttributeInstance lifeSteal = player.getAttribute(Attributes.LIFE_STEAL.get());
            event.setAmount((float) (event.getAmount() + lifeSteal.getValue()));
            player.heal((float) lifeSteal.getValue());

            AttributeInstance vampire = player.getAttribute(Attributes.VAMPIRE.get());
            player.heal((float) (event.getAmount() * vampire.getValue()));


        }
    }

    @SubscribeEvent
    public static void onCriticalHit(CriticalHitEvent event) {
        Player player = event.getEntity();
        double criticalDamageCoefficients = AttributeHandler.getCriticalDamageCoefficients(player);
        event.setDamageModifier((float) criticalDamageCoefficients);
    }

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
        AttributeInstance attribute = player.getAttribute(Attributes.MINING_SPEED.get());
        event.setNewSpeed((float) (event.getOriginalSpeed() * attribute.getValue()));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingHeal(LivingHealEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player player) {
            AttributeInstance attribute = player.getAttribute(Attributes.HEALING_RECEIVED.get());
            event.setAmount((float) (event.getAmount() * attribute.getValue()));
        }
    }
}
