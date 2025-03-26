package org.yunxi.remodifier.common.attribute;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.yunxi.remodifier.Remodifier;

public class Attributes {
    public static final DeferredRegister<Attribute> ATTRIBUTE_DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, Remodifier.MODID);

    public static final RegistryObject<Attribute> CRITICAL_HIT = ATTRIBUTE_DEFERRED_REGISTER.register("crit_chance",
            () -> new BasedAttribute("attribute.remodifier.crit_chance", 0.1, 0, 100).setSyncable(true));

    public static final RegistryObject<Attribute> CRITICAL_DAMAGE = ATTRIBUTE_DEFERRED_REGISTER.register("crit_damage",
            () -> new BasedAttribute("attribute.remodifier.crit_damage", 2.0, 1, 1000).setSyncable(true));

}
