package org.yunxi.remodifier;

import com.mojang.logging.LogUtils;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;
import org.yunxi.remodifier.common.block.ReforgedTableBlock;
import org.yunxi.remodifier.common.config.JsonConfigInitialier;
import org.yunxi.remodifier.common.config.toml.Config;
import org.yunxi.remodifier.common.config.toml.ReforgeConfig;
import org.yunxi.remodifier.common.config.toml.modifiers.*;
import org.yunxi.remodifier.common.curios.ICurioProxy;
import org.yunxi.remodifier.common.item.ModifierBookItem;
import org.yunxi.remodifier.common.network.NetworkHandler;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(Remodifier.MODID)
public class Remodifier {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "remodifier";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<Block> BLOCK_DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final RegistryObject<Block> REFORGED_TABLE;
    public static final DeferredRegister<Item> ITEM_DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final RegistryObject<Item> MODIFIER_BOOK;
    public static final RegistryObject<Item> REFORGED_TABLE_ITEM;
    public static ICurioProxy CURIO_PROXY;
    public static CreativeModeTab GROUP_BOOKS;

    @SuppressWarnings("removal")
    public Remodifier() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        final IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        final ModLoadingContext modLoadingContext = ModLoadingContext.get();
        final ModConfig.Type common = ModConfig.Type.COMMON;
        BLOCK_DEFERRED_REGISTER.register(modEventBus);
        ITEM_DEFERRED_REGISTER.register(modEventBus);


        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
        NetworkHandler.register();

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

    private static Boolean isCuriosLoaded = null;

    public static boolean isCuriosLoaded() {
        if (isCuriosLoaded == null) isCuriosLoaded = ModList.get().isLoaded("curios");
        return isCuriosLoaded;
    }

    private void setup(final FMLCommonSetupEvent event) {
//        Modifiers.initialize();
        event.enqueueWork(() -> {
            if (isCuriosLoaded()) {
                try {
                    CURIO_PROXY = (ICurioProxy) Class.forName("org.yunxi.remodifier.common.curios.CurioCompat").getDeclaredConstructor().newInstance();
                    MinecraftForge.EVENT_BUS.register(CURIO_PROXY);
                } catch (Exception e) {
                    LOGGER.error("Remodifier failed to load Curios integration.", e);
                }
            }
            if (CURIO_PROXY == null) CURIO_PROXY = new ICurioProxy() {};
        });
    }

    static {
        REFORGED_TABLE = BLOCK_DEFERRED_REGISTER.register("reforged_table", ReforgedTableBlock::new);
        REFORGED_TABLE_ITEM = ITEM_DEFERRED_REGISTER.register("reforged_table", () -> new BlockItem(REFORGED_TABLE.get(), new Item.Properties()));
        MODIFIER_BOOK = ITEM_DEFERRED_REGISTER.register("modifier_book", ModifierBookItem::new);
    }
}
