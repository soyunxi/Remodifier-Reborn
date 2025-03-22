package org.yunxi.remodifier.common.modifier;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import org.yunxi.remodifier.Remodifier;
import org.yunxi.remodifier.common.config.toml.modifiers.CuriosModifiersConfig;

import java.util.Map;

public class Modifiers {
    public static final Map<ResourceLocation, Modifier> MODIFIERS = new Object2ObjectOpenHashMap<>();

    public static final Modifier NONE = new Modifier.ModifierBuilder(ResourceLocation.fromNamespaceAndPath(Remodifier.MODID, "none"), "modifier_none", ModifierType.BOTH).setWeight(0).setRarity(-1).build();

    static {
        MODIFIERS.put(NONE.name, NONE);
    }

    public static final ModifierPool curioPool = new ModifierPool(stack -> Remodifier.CURIO_PROXY.isModifiableCurio(stack));

    public static final ModifierPool armorPool = new ModifierPool(stack -> stack.getItem() instanceof ArmorItem || CuriosModifiersConfig.WHETHER_OR_NOT_CURIOS_USE_ARMOR_MODIFIERS.get() && Remodifier.CURIO_PROXY.isModifiableCurio(stack));

    public static final ModifierPool toolPool = new ModifierPool(stack -> {
        Item item = stack.getItem();
        if (item instanceof SwordItem) return true;
        return item instanceof DiggerItem;
    });

    public static final ModifierPool bowPool = new ModifierPool(stack -> stack.getItem() instanceof ProjectileWeaponItem);

    public static final ModifierPool shieldPool = new ModifierPool(stack -> stack.getItem() instanceof ShieldItem);




}
