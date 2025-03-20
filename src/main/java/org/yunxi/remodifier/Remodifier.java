package org.yunxi.remodifier;

import com.mojang.logging.LogUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;
import org.yunxi.remodifier.common.config.JsonConfigInitialier;
import org.yunxi.remodifier.common.config.toml.Config;
import org.yunxi.remodifier.common.config.toml.ReforgeConfig;
import org.yunxi.remodifier.common.config.toml.modifiers.*;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(Remodifier.MODID)
public class Remodifier {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "remodifier";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    @SuppressWarnings("removal")
    public Remodifier() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        final IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        final ModLoadingContext modLoadingContext = ModLoadingContext.get();
        final ModConfig.Type common = ModConfig.Type.COMMON;


        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);

        modLoadingContext.registerConfig(common, ArmorModifiersConfig.CONFIG, "remodifier/modifiers/armor-modifiers.toml");
        modLoadingContext.registerConfig(common, ToolModifiersConfig.CONFIG, "remodifier/modifiers/tool-modifiers.toml");
        modLoadingContext.registerConfig(common, BowModifiersConfig.CONFIG, "remodifier/modifiers/bow-modifiers.toml");
        modLoadingContext.registerConfig(common, ShieldModifiersConfig.CONFIG, "remodifier/modifiers/shield-modifiers.toml");
        modLoadingContext.registerConfig(common, CuriosModifiersConfig.CONFIG, "remodifier/modifiers/curios-modifiers.toml");
        modLoadingContext.registerConfig(common, ReforgeConfig.CONFIG, "remodifier/reforge.toml");
        modLoadingContext.registerConfig(common, Config.CONFIG, "remodifier/config.toml");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
        LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        new JsonConfigInitialier().init();
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }
    }
}
