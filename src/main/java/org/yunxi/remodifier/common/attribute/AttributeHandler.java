package org.yunxi.remodifier.common.attribute;

import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;

public class AttributeHandler {
    public static double getCriticalDamageCoefficients(Player player) {
        AttributeInstance criticalDamage = player.getAttribute(Attributes.CRITICAL_DAMAGE.get());
        AttributeInstance criticalHit = player.getAttribute(Attributes.CRITICAL_HIT.get());
        double out = criticalDamage.getValue();
        if (criticalHit.getValue() > 1) {
            out += criticalHit.getValue() / 10;
        }
        return out;
    }
}
