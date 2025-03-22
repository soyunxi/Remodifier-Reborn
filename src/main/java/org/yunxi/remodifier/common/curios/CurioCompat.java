package org.yunxi.remodifier.common.curios;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.yunxi.remodifier.common.modifier.Modifier;
import org.yunxi.remodifier.common.modifier.ModifierHandler;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.event.CurioChangeEvent;

import java.util.concurrent.ThreadLocalRandom;

public class CurioCompat implements ICurioProxy {
    @SubscribeEvent
    public void onCurioChange(CurioChangeEvent event) {
        LivingEntity entity = event.getEntity();
        ItemStack to = event.getTo();
        String identifier = event.getIdentifier();
        int slot = event.getSlotIndex();

        Modifier modFrom = ModifierHandler.getModifier(event.getFrom());
        if (modFrom != null) ModifierHandler.removeCurioModifier(entity, modFrom, identifier, slot);
        Modifier modifier = ModifierHandler.getModifier(to);
        if (modifier == null) {
            modifier = ModifierHandler.rollModifier(to, ThreadLocalRandom.current());
            if (modifier == null) return;
            ModifierHandler.setModifier(to, modifier);
        }
        ModifierHandler.applyCurioModifier(entity, modifier, identifier, slot);
    }

    @Override
    public boolean isModifiableCurio(ItemStack stack) {
        return !CuriosApi.getCuriosHelper().getCurioTags(stack.getItem()).isEmpty();
    }
}
