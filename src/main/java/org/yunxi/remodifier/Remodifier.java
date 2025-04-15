package org.yunxi.remodifier;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;
import org.yunxi.remodifier.client.ReforgeTableContainer;
import org.yunxi.remodifier.client.ReforgeTableScreen;
import org.yunxi.remodifier.client.events.ClientEvents;
import org.yunxi.remodifier.common.attribute.Attributes;
import org.yunxi.remodifier.common.block.ReforgedTableBlock;
import org.yunxi.remodifier.common.block.ReforgedTableBlockEntity;
import org.yunxi.remodifier.common.config.JsonConfigInitialier;
import org.yunxi.remodifier.common.config.toml.ReModifierConfig;
import org.yunxi.remodifier.common.config.toml.ReforgeConfig;
import org.yunxi.remodifier.common.config.toml.modifiers.*;
import org.yunxi.remodifier.common.curios.ICurioProxy;
import org.yunxi.remodifier.common.events.CommonEvents;
import org.yunxi.remodifier.common.item.ModifierBookItem;
import org.yunxi.remodifier.common.modifier.Modifiers;
import org.yunxi.remodifier.common.network.NetworkHandler;


@Mod(Remodifier.MODID)
public class Remodifier {

    public static final String MODID = "remodifier";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<Block> BLOCK_DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final RegistryObject<Block> REFORGED_TABLE;
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPE_DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID);
    public static final RegistryObject<BlockEntityType<ReforgedTableBlockEntity>> REFORGED_TABLE_TYPE;
    public static final DeferredRegister<Item> ITEM_DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final RegistryObject<Item> MODIFIER_BOOK;
    public static final RegistryObject<Item> REFORGED_TABLE_ITEM;
    public static final DeferredRegister<MenuType<?>> MENU_DEFERRED_REGISTER = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MODID);
    public static final RegistryObject<MenuType<ReforgeTableContainer>> REFORGED_TABLE_MENU;
    public static ICurioProxy CURIO_PROXY;

    @SuppressWarnings("removal")
    public Remodifier() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        final IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        final ModLoadingContext modLoadingContext = ModLoadingContext.get();
        final ModConfig.Type common = ModConfig.Type.COMMON;
        BLOCK_DEFERRED_REGISTER.register(modEventBus);
        ITEM_DEFERRED_REGISTER.register(modEventBus);
        BLOCK_ENTITY_TYPE_DEFERRED_REGISTER.register(modEventBus);
        MENU_DEFERRED_REGISTER.register(modEventBus);
        Attributes.ATTRIBUTE_DEFERRED_REGISTER.register(modEventBus);


        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
        NetworkHandler.register();
        forgeEventBus.register(CommonEvents.class);
        if (FMLLoader.getDist().isClient()) forgeEventBus.register(ClientEvents.class);

        modLoadingContext.registerConfig(common, ArmorModifiersConfig.CONFIG, "remodifier/modifiers/armor-modifiers.toml");
        modLoadingContext.registerConfig(common, ToolModifiersConfig.CONFIG, "remodifier/modifiers/tool-modifiers.toml");
        modLoadingContext.registerConfig(common, WeaponModifiersConfig.CONFIG, "remodifier/modifiers/weapon-modifiers.toml");
        modLoadingContext.registerConfig(common, BowModifiersConfig.CONFIG, "remodifier/modifiers/bow-modifiers.toml");
        modLoadingContext.registerConfig(common, ShieldModifiersConfig.CONFIG, "remodifier/modifiers/shield-modifiers.toml");
        modLoadingContext.registerConfig(common, CuriosModifiersConfig.CONFIG, "remodifier/modifiers/curios-modifiers.toml");
        modLoadingContext.registerConfig(common, ReforgeConfig.CONFIG, "remodifier/reforge.toml");
        modLoadingContext.registerConfig(common, ReModifierConfig.CONFIG, "remodifier/config.toml");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        new JsonConfigInitialier().init();
        Modifiers.initialize();
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

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
    }

    @SuppressWarnings("removal")
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEvents {
        @SubscribeEvent
        public static void onRegister(RegisterEvent event) {
            event.register(Registries.CREATIVE_MODE_TAB, new ResourceLocation(MODID, "remodifier_books"), () -> CreativeModeTab.builder()
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(new ItemStack(REFORGED_TABLE_ITEM.get()));
                        ModifierBookItem.getStacks().forEach(output::accept);
                    })
                    .icon(() -> new ItemStack(MODIFIER_BOOK.get()))
                    .title(Component.translatable("itemGroup.remodifier.books"))
                    .build());
        }

        @SubscribeEvent
        public static void applyAttribs(EntityAttributeModificationEvent event) {
            event.add(EntityType.PLAYER, Attributes.CRITICAL_HIT.get());
            event.add(EntityType.PLAYER, Attributes.CRITICAL_DAMAGE.get());
            event.add(EntityType.PLAYER, Attributes.NO_CONSUMPTION.get());
            event.add(EntityType.PLAYER, Attributes.POWER_SPEED.get());
            event.add(EntityType.PLAYER, Attributes.BULLET_SPEED.get());
            event.add(EntityType.PLAYER, Attributes.BULLET_DAMAGE.get());
            event.add(EntityType.PLAYER, Attributes.LIFE_STEAL.get());
            event.add(EntityType.PLAYER, Attributes.VAMPIRE.get());
            event.add(EntityType.PLAYER, Attributes.MINING_SPEED.get());
            event.add(EntityType.PLAYER, Attributes.HEALING_RECEIVED.get());
        }

    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                MenuScreens.register(
                        Remodifier.REFORGED_TABLE_MENU.get(),
                        ReforgeTableScreen::new // 确保构造函数匹配
                );
            });
        }
    }

    private static Boolean isCuriosLoaded = null;

    public static boolean isCuriosLoaded() {
        if (isCuriosLoaded == null) isCuriosLoaded = ModList.get().isLoaded("curios");
        return isCuriosLoaded;
    }

    static {
        REFORGED_TABLE = BLOCK_DEFERRED_REGISTER.register("reforged_table", ReforgedTableBlock::new);
        REFORGED_TABLE_TYPE = BLOCK_ENTITY_TYPE_DEFERRED_REGISTER.register("reforged_table", () -> BlockEntityType.Builder.of(ReforgedTableBlockEntity::new, REFORGED_TABLE.get()).build(null));
        REFORGED_TABLE_ITEM = ITEM_DEFERRED_REGISTER.register("reforged_table", () -> new BlockItem(REFORGED_TABLE.get(), new Item.Properties()));
        MODIFIER_BOOK = ITEM_DEFERRED_REGISTER.register("modifier_book", ModifierBookItem::new);
        REFORGED_TABLE_MENU = MENU_DEFERRED_REGISTER.register("reforged_table", () -> IForgeMenuType.create((windowId, inv, data) -> new ReforgeTableContainer(windowId, inv.player, data.readBlockPos())));
    }
}
