package org.yunxi.remodifier.common.config.toml.modifiers;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import org.yunxi.remodifier.Remodifier;

import java.util.List;

@Mod.EventBusSubscriber(modid = Remodifier.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ArmorModifiersConfig {
    public static ForgeConfigSpec CONFIG;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> NAMES;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> WEIGHTS;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> RARITIES;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> ATTRIBUTES;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> AMOUNTS;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> OPERATIONS_IDS;


    static {
        ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
        BUILDER.push("Modifiers for armors");
        BUILDER.comment("This configuration file is based on index. it means, 'half_hearted' -> '300' -> 'generic.max_health' -> '1.0' -> '0' is the first index.", "You may need to make a resource pack to save the customization-required translation key for the attributes if the mod author didn't do that, or your customization on other mods' attribute may not look well", "A hint on the translation key format: attribute.modxxx.attributexxx, e.g. attribute.minecraft.generic.attack_damage");
        NAMES = BUILDER.comment("The name of the modifier").defineList("NAMES", Lists.newArrayList("half_hearted", "hearty", "hard", "guarding", "armored", "warding", "jagged", "spiked", "angry", "menacing", "brisk", "fleeting", "hasty", "quick", "wild", "rash", "intrepid"), o -> true);
        WEIGHTS = BUILDER.comment("The weight of the modifier in the modifiers pool").defineList("WEIGHTS", Lists.newArrayList("300", "100", "300", "200", "100", "200", "200", "200", "100", "100", "200", "200", "100", "100", "200", "200", "100"), o -> true);
        RARITIES = BUILDER.comment("The rarity of the modifier in the modifiers pool").defineList("RARITIES", Lists.newArrayList("3", "1", "3", "2", "1", "2", "2", "2", "1", "1", "2", "2", "1", "1", "2", "2", "1"), o -> true);
        ATTRIBUTES = BUILDER.comment("The attribute of the modifier has. One modifier can have multiple attributes. Use ';' to split different attributes").defineList("ATTRIBUTES", Lists.newArrayList("minecraft:generic.max_health", "minecraft:generic.max_health", "minecraft:generic.armor", "minecraft:generic.armor", "minecraft:generic.armor", "minecraft:generic.armor_toughness", "minecraft:generic.attack_damage", "minecraft:generic.attack_damage", "minecraft:generic.attack_damage", "minecraft:generic.attack_damage", "minecraft:generic.movement_speed", "minecraft:generic.movement_speed", "minecraft:generic.movement_speed", "minecraft:generic.movement_speed", "minecraft:generic.attack_speed", "minecraft:generic.attack_speed", "minecraft:generic.attack_speed"), o -> true);
        AMOUNTS = BUILDER.comment("The amount used to calculate the attribute effect. Also can be multiple. Use ';' to split").defineList("AMOUNTS", Lists.newArrayList("1.0", "2.0", "1.0", "1.5", "2.0", "1.0", "0.01", "0.02", "0.03", "0.04", "0.01", "0.02","0.03", "0.04", "0.01", "0.02", "0.03"), o -> true);
        OPERATIONS_IDS = BUILDER.comment("The operation ID of the attribute calculation. Can be three values: 0,1,2. 0 is ADDITION. 1 is MULTIPLY_BASE. 2 is MULTIPLY_TOTAL. you can refer to the calculation of the attributes already in the game").defineList("OPERATIONS_IDS", Lists.newArrayList("0", "0", "0", "0", "0", "0", "2", "2", "2", "2", "2", "2", "2","2", "2", "2", "2"), o -> true);
        BUILDER.pop();
        CONFIG = BUILDER.build();
    }
}
