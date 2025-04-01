package org.yunxi.remodifier.common.attribute;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.spongepowered.asm.mixin.injection.At;
import org.yunxi.remodifier.Remodifier;

public class Attributes {
    public static final DeferredRegister<Attribute> ATTRIBUTE_DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, Remodifier.MODID);

    public static final RegistryObject<Attribute> CRITICAL_HIT = ATTRIBUTE_DEFERRED_REGISTER.register("crit_chance",
            () -> new BasedAttribute("attribute.remodifier.crit_chance", 0.1, 0, 100).setSyncable(true));

    public static final RegistryObject<Attribute> CRITICAL_DAMAGE = ATTRIBUTE_DEFERRED_REGISTER.register("crit_damage",
            () -> new BasedAttribute("attribute.remodifier.crit_damage", 1.5, 1, 1000).setSyncable(true));

    public static final RegistryObject<Attribute> NO_CONSUMPTION = ATTRIBUTE_DEFERRED_REGISTER.register("no_consumption",
            () -> new BasedAttribute("attribute.remodifier.no_consumption", 0, 0, 1).setSyncable(true));

    public static final RegistryObject<Attribute> POWER_SPEED = ATTRIBUTE_DEFERRED_REGISTER.register("power_speed",
            () -> new BasedAttribute("attribute.remodifier.power_speed",  1, 0, 10).setSyncable(true));

    public static final RegistryObject<Attribute> BULLET_SPEED = ATTRIBUTE_DEFERRED_REGISTER.register("bullet_speed",
            () -> new BasedAttribute("attribute.remodifier.bullet_speed", 1, 0, 100).setSyncable(true));

    public static final RegistryObject<Attribute> BULLET_DAMAGE = ATTRIBUTE_DEFERRED_REGISTER.register("bullet_damage",
            () -> new BasedAttribute("attribute.remodifier.bullet_damage", 1, 0, 1000).setSyncable(true));

    public static final RegistryObject<Attribute> LIFE_STEAL = ATTRIBUTE_DEFERRED_REGISTER.register("life_steal",
            () -> new RangedAttribute("attribute.remodifier.life_steal", 0, 0, 10000).setSyncable(true));

}
