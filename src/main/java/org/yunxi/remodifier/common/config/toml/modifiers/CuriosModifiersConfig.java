package org.yunxi.remodifier.common.config.toml.modifiers;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class CuriosModifiersConfig {
    public static ForgeConfigSpec CONFIG;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> NAMES;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> WEIGHTS;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> RARITIES;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> ATTRIBUTES;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> AMOUNTS;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> OPERATIONS_IDS;
    public static ForgeConfigSpec.BooleanValue WHETHER_OR_NOT_CURIOS_USE_ARMOR_MODIFIERS;

    static {
        ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
        BUILDER.push("Modifiers for curios");
        BUILDER.comment("You may need to make a resource pack to save the customization-required translation key for the attributes if the mod author didn't do that, or your customization on other mods' attribute may not look well", "A hint on the translation key format: attribute.modxxx.attributexxx, e.g. attribute.minecraft.generic.attack_damage");
        NAMES = BUILDER.comment("The name of the modifier").defineList("NAMES", Lists.newArrayList(), o -> true);
        WEIGHTS = BUILDER.comment("The weight of the modifier in the modifiers pool").defineList("WEIGHTS", Lists.newArrayList(), o -> true);
        RARITIES = BUILDER.comment("The rarity of the modifier in the modifiers pool").defineList("RARITIES", Lists.newArrayList(), o -> true);
        ATTRIBUTES = BUILDER.comment("The attribute of the modifier has. One modifier can have multiple attributes. Use ';' to split different attributes").defineList("ATTRIBUTES", Lists.newArrayList(), o -> true);
        AMOUNTS = BUILDER.comment("The amount used to calculate the attribute effect. Also can be multiple. Use ';' to split").defineList("AMOUNTS", Lists.newArrayList(), o -> true);
        OPERATIONS_IDS = BUILDER.comment("The operation ID of the attribute calculation. Can be three values: 0,1,2. 0 is ADDITION. 1 is MULTIPLY_BASE. 2 is MULTIPLY_TOTAL. you can refer to the calculation of the attributes already in the game").defineList("OPERATIONS_IDS", Lists.newArrayList(), o -> true);
        WHETHER_OR_NOT_CURIOS_USE_ARMOR_MODIFIERS = BUILDER.comment("When enabled, the registries of curios modifiers will be ignored and your curio items will use armor modifiers.").define("WhetherOrNotCuriosUseArmorModifiers", false);
        BUILDER.pop();
        CONFIG = BUILDER.build();
    }
}
