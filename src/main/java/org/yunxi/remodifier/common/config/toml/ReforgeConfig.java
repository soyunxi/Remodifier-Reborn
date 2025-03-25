package org.yunxi.remodifier.common.config.toml;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class ReforgeConfig {
    public static ForgeConfigSpec CONFIG;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> UNIVERSAL_REFORGE_ITEM;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> UNIVERSAL_REFORGE_ITEM_RARITY;
    public static ForgeConfigSpec.BooleanValue DISABLE_REPAIR_REFORGED;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("Remodifier");
        UNIVERSAL_REFORGE_ITEM = builder.comment("Universal reforge item").defineList("universalReforgeItem", Lists.newArrayList("minecraft:gold_ingot", "minecraft:diamond"), o -> true);
        UNIVERSAL_REFORGE_ITEM_RARITY = builder.comment("Universal reforge item rarity").defineList("universalReforgeItemRarity", Lists.newArrayList("0", "0"), o -> true);
        DISABLE_REPAIR_REFORGED = builder.define("DisableRepairingReforging", false);
        builder.pop();
        CONFIG = builder.build();
    }
}
