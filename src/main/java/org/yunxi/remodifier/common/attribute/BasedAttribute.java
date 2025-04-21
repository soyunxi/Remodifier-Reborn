package org.yunxi.remodifier.common.attribute;

import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class BasedAttribute extends RangedAttribute implements PercentageFormattable {
    public BasedAttribute(String pDescriptionId, double pDefaultValue, double pMin, double pMax) {
        super(pDescriptionId, pDefaultValue, pMin, pMax);
    }
}
