package org.yunxi.remodifier.common.attribute;

import dev.shadowsoffire.attributeslib.api.IFormattableAttribute;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

public class BasedAttribute extends RangedAttribute implements IFormattableAttribute {
    public BasedAttribute(String pDescriptionId, double pDefaultValue, double pMin, double pMax) {
        super(pDescriptionId, pDefaultValue, pMin, pMax);
    }

    @Override
    public MutableComponent toValueComponent(AttributeModifier.Operation op, double value, TooltipFlag flag) {
        return Component.translatable("attributeslib.value.percent", ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(value * 100));
    }
}
