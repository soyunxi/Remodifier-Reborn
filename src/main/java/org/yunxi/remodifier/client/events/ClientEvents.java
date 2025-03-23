package org.yunxi.remodifier.client.events;

import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.yunxi.remodifier.common.modifier.Modifier;
import org.yunxi.remodifier.common.modifier.ModifierHandler;

public class ClientEvents {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onGetTooltip(ItemTooltipEvent event) {
        Modifier modifier = ModifierHandler.getModifier(event.getItemStack());
        if (modifier != null) {
            event.getToolTip().addAll(modifier.getInfoLines());
        }
    }
}
