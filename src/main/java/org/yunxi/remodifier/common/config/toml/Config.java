package org.yunxi.remodifier.common.config.toml;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class Config {
    public static ForgeConfigSpec CONFIG;
    public static ForgeConfigSpec.BooleanValue QUALITY;
    public static ForgeConfigSpec.BooleanValue SHOULD_GENERATE_VIOLENT_JSON;
    public static ForgeConfigSpec.BooleanValue CRASHTHEGAME_IFCONFIG_HASERRORS;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("Remodifier");
        QUALITY = builder.comment("Whether or not to enable the quality substitution Modifiers to become a mod with mods").define("qualitySubstituteModifier", false);
        SHOULD_GENERATE_VIOLENT_JSON = builder.comment("Specifies whether to generate a JSON instance file").define("ShouldGenerateViolentJson", true);
        CRASHTHEGAME_IFCONFIG_HASERRORS = builder.comment("If you open the configuration file incorrectly, it will crash directly").define("CrashTheGameIfConfigHasErrors", true);
        builder.pop();
        CONFIG = builder.build();
    }
}
