package org.yunxi.remodifier.common.modifier;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.yunxi.remodifier.Remodifier;
import org.yunxi.remodifier.common.config.toml.ReModifierConfig;
import org.yunxi.remodifier.common.config.toml.modifiers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("removal")
public class Modifiers {
    public static final Map<ResourceLocation, Modifier> MODIFIERS = new Object2ObjectOpenHashMap<>();

    public static final Modifier NONE = new Modifier.ModifierBuilder(new ResourceLocation(Remodifier.MODID, "none"), "modifier_none", ModifierType.BOTH).setWeight(0).setRarity(-1).build();

    static {
        MODIFIERS.put(NONE.name, NONE);
    }

    public static final ModifierPool curioPool = new ModifierPool(stack -> Remodifier.CURIO_PROXY.isModifiableCurio(stack));

    public static final ModifierPool armorPool = new ModifierPool(stack -> stack.getItem() instanceof ArmorItem || CuriosModifiersConfig.WHETHER_OR_NOT_CURIOS_USE_ARMOR_MODIFIERS.get() && Remodifier.CURIO_PROXY.isModifiableCurio(stack));

    public static final ModifierPool toolPool = new ModifierPool(stack -> {
        Item item = stack.getItem();
        if (WeaponModifiersConfig.WHETHER_OR_NOT_WEAPON_USE_TOOL_MODIFIERS.get() && item instanceof SwordItem) return true;
        return item instanceof DiggerItem;
    });

    public static final ModifierPool weaponPool = new ModifierPool(stack -> {
        Item item = stack.getItem();
        if (!WeaponModifiersConfig.WHETHER_OR_NOT_WEAPON_USE_TOOL_MODIFIERS.get()) {
            return item instanceof SwordItem;
        }
        return false;
    });

    public static final ModifierPool bowPool = new ModifierPool(stack -> stack.getItem() instanceof ProjectileWeaponItem);

    public static final ModifierPool shieldPool = new ModifierPool(stack -> stack.getItem() instanceof ShieldItem);

    @Contract("_ -> new")
    private static Modifier.@NotNull ModifierBuilder equipped(String name) {
        return new Modifier.ModifierBuilder(new ResourceLocation(Remodifier.MODID, name), "modifier_" + name, ModifierType.EQUIPPED);
    }

    @Contract("_ -> new")
    private static Modifier.@NotNull ModifierBuilder held(String name) {
        return new Modifier.ModifierBuilder(new ResourceLocation(Remodifier.MODID, name), "modifier_" + name, ModifierType.HELD);
    }

    private static void addCurio(Modifier modifier) {
        MODIFIERS.put(modifier.name, modifier);
        curioPool.add(modifier);
    }
    private static void addArmor(Modifier modifier) {
        MODIFIERS.put(modifier.name, modifier);
        armorPool.add(modifier);
    }

    private static void addTool(Modifier modifier) {
        MODIFIERS.put(modifier.name, modifier);
        toolPool.add(modifier);
    }

    private static void addWeapon(Modifier modifier) {
        MODIFIERS.put(modifier.name, modifier);
        weaponPool.add(modifier);
    }

    private static void addBow(Modifier modifier) {
        MODIFIERS.put(modifier.name, modifier);
        bowPool.add(modifier);
    }

    private static void addShield(Modifier modifier) {
        MODIFIERS.put(modifier.name, modifier);
        shieldPool.add(modifier);
    }

    @Contract("_, _ -> new")
    private static Modifier.@NotNull AttributeModifierSupplier mod(double amount, AttributeModifier.Operation op) {
        return new Modifier.AttributeModifierSupplier(amount, op);
    }

    private static Modifier.AttributeModifierSupplier @NotNull [] mods(String @NotNull [] amounts, String[] ops) {
        Modifier.AttributeModifierSupplier[] suppliers = new Modifier.AttributeModifierSupplier[amounts.length];
        for (int index = 0; index < amounts.length; index++) {
            suppliers[index] = new Modifier.AttributeModifierSupplier(Double.parseDouble(amounts[index]), AttributeModifier.Operation.fromValue(Integer.parseInt(ops[index])));
        }
        return suppliers;
    }

    @Contract("_, _ -> new")
    private static @NotNull List<? extends String> merge(@NotNull List<? extends String> iterable1, @NotNull List<? extends String> iterable2) {
        List<String> list = new ArrayList<>(iterable1);
        list.addAll(iterable2);
        return list;
    }

    private static void initBowModifiers() {
        List<? extends String> MODIFIERS_NAMES = merge(BowModifiersConfig.NAMES.get(), ModifierHandler.getBowNames());
        List<? extends String> MODIFIERS_WEIGHTS = merge(BowModifiersConfig.WEIGHTS.get(), ModifierHandler.getBowWeights());
        List<? extends String> MODIFIERS_RARITIES = merge(BowModifiersConfig.RARITIES.get(), ModifierHandler.getBowRarities());
        List<? extends String> MODIFIERS_ATTRIBUTES = merge(BowModifiersConfig.ATTRIBUTES.get(), ModifierHandler.getBowAttributes());
        List<? extends String> MODIFIERS_AMOUNTS = merge(BowModifiersConfig.AMOUNTS.get(), ModifierHandler.getBowAmounts());
        List<? extends String> MODIFIERS_OPERATIONS_IDS = merge(BowModifiersConfig.OPERATIONS_IDS.get(), ModifierHandler.getBowOperationsIDS());
        for (int index = 0; index < MODIFIERS_NAMES.size(); index++) {
            String name = MODIFIERS_NAMES.get(index);
            String weight = MODIFIERS_WEIGHTS.get(index);
            String rarity = MODIFIERS_RARITIES.get(index);
            String attribute = MODIFIERS_ATTRIBUTES.get(index);
            String amount = MODIFIERS_AMOUNTS.get(index);
            String operations_id = MODIFIERS_OPERATIONS_IDS.get(index);
            if (attribute.contains(";")) {
                String[] attributes = attribute.split(";");
                String[] amounts = amount.split(";");
                String[] operations_ids = operations_id.split(";");
                addBow(held(name).addModifiers(attributes, mods(amounts, operations_ids)).setWeight(Integer.parseInt(weight)).build());
            } else {
                Attribute entityAttribute = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(attribute));
                if (entityAttribute == null) {
                    Remodifier.LOGGER.error("Invalid value: {}", attribute);
                    return;
                }
                addBow(held(name).setWeight(Integer.parseInt(weight)).setRarity(Integer.parseInt(rarity)).addModifier(entityAttribute, mod(Double.parseDouble(amount), AttributeModifier.Operation.fromValue(Integer.parseInt(operations_id)))).build());
            }
        }
    }

    private static void initShieldModifiers() {
        List<? extends String> MODIFIERS_NAMES = merge(ShieldModifiersConfig.NAMES.get(), ModifierHandler.getShieldNames());
        List<? extends String> MODIFIERS_WEIGHTS = merge(ShieldModifiersConfig.WEIGHTS.get(), ModifierHandler.getShieldWeights());
        List<? extends String> MODIFIERS_RARITIES = merge(ShieldModifiersConfig.RARITIES.get(), ModifierHandler.getShieldRarities());
        List<? extends String> MODIFIERS_ATTRIBUTES = merge(ShieldModifiersConfig.ATTRIBUTES.get(), ModifierHandler.getShieldAttributes());
        List<? extends String> MODIFIERS_AMOUNTS = merge(ShieldModifiersConfig.AMOUNTS.get(), ModifierHandler.getShieldAmounts());
        List<? extends String> MODIFIERS_OPERATIONS_IDS = merge(ShieldModifiersConfig.OPERATIONS_IDS.get(), ModifierHandler.getShieldOperationsIDS());
        for (int index = 0; index < MODIFIERS_NAMES.size(); index++) {
            String name = MODIFIERS_NAMES.get(index);
            String weight = MODIFIERS_WEIGHTS.get(index);
            String rarity = MODIFIERS_RARITIES.get(index);
            String attribute = MODIFIERS_ATTRIBUTES.get(index);
            String amount = MODIFIERS_AMOUNTS.get(index);
            String operations_id = MODIFIERS_OPERATIONS_IDS.get(index);
            if (attribute.contains(";")) {
                String[] attributes = attribute.split(";");
                String[] amounts = amount.split(";");
                String[] operations_ids = operations_id.split(";");
                addShield(held(name).addModifiers(attributes, mods(amounts, operations_ids)).setWeight(Integer.parseInt(weight)).build());
            } else {
                Attribute entityAttribute = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(attribute));
                if (entityAttribute == null) {
                    Remodifier.LOGGER.error("Invalid value: {}", attribute);
                    return;
                }
                addShield(held(name).setWeight(Integer.parseInt(weight)).setRarity(Integer.parseInt(rarity)).addModifier(entityAttribute, mod(Double.parseDouble(amount), AttributeModifier.Operation.fromValue(Integer.parseInt(operations_id)))).build());
            }
        }
    }

    private static void initToolModifiers() {
        List<? extends String> MODIFIERS_NAMES = merge(ToolModifiersConfig.NAMES.get(), ModifierHandler.getToolNames());
        List<? extends String> MODIFIERS_WEIGHTS = merge(ToolModifiersConfig.WEIGHTS.get(), ModifierHandler.getToolWeights());
        List<? extends String> MODIFIERS_RARITIES = merge(ToolModifiersConfig.RARITIES.get(), ModifierHandler.getToolRarities());
        List<? extends String> MODIFIERS_ATTRIBUTES = merge(ToolModifiersConfig.ATTRIBUTES.get(), ModifierHandler.getToolAttributes());
        List<? extends String> MODIFIERS_AMOUNTS = merge(ToolModifiersConfig.AMOUNTS.get(), ModifierHandler.getToolAmounts());
        List<? extends String> MODIFIERS_OPERATIONS_IDS = merge(ToolModifiersConfig.OPERATIONS_IDS.get(), ModifierHandler.getToolOperationsIDS());
        for (int index = 0; index < MODIFIERS_NAMES.size(); index++) {
            String name = MODIFIERS_NAMES.get(index);
            String weight = MODIFIERS_WEIGHTS.get(index);
            String rarity = MODIFIERS_RARITIES.get(index);
            String attribute = MODIFIERS_ATTRIBUTES.get(index);
            String amount = MODIFIERS_AMOUNTS.get(index);
            String operations_id = MODIFIERS_OPERATIONS_IDS.get(index);
            if (attribute.contains(";")) {
                String[] attributes = attribute.split(";");
                String[] amounts = amount.split(";");
                String[] operations_ids = operations_id.split(";");
                addTool(held(name).addModifiers(attributes, mods(amounts, operations_ids)).setWeight(Integer.parseInt(weight)).build());
            } else {
                Attribute entityAttribute = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(attribute));
                if (entityAttribute == null) {
                    Remodifier.LOGGER.error("Invalid value: {}", attribute);
                    return;
                }
                addTool(held(name).setWeight(Integer.parseInt(weight)).setRarity(Integer.parseInt(rarity)).addModifier(entityAttribute, mod(Double.parseDouble(amount), AttributeModifier.Operation.fromValue(Integer.parseInt(operations_id)))).build());
            }
        }
    }

    private static void initArmorsModifiers() {
        List<? extends String> MODIFIERS_NAMES = merge(ArmorModifiersConfig.NAMES.get(), ModifierHandler.getArmorsNames());
        List<? extends String> MODIFIERS_WEIGHTS = merge(ArmorModifiersConfig.WEIGHTS.get(), ModifierHandler.getArmorsWeights());
        List<? extends String> MODIFIERS_RARITIES = merge(ArmorModifiersConfig.RARITIES.get(), ModifierHandler.getArmorsRarities());
        List<? extends String> MODIFIERS_ATTRIBUTES = merge(ArmorModifiersConfig.ATTRIBUTES.get(), ModifierHandler.getArmorsAttributes());
        List<? extends String> MODIFIERS_AMOUNTS = merge(ArmorModifiersConfig.AMOUNTS.get(), ModifierHandler.getArmorsAmounts());
        List<? extends String> MODIFIERS_OPERATIONS_IDS = merge(ArmorModifiersConfig.OPERATIONS_IDS.get(), ModifierHandler.getArmorsOperationsIDS());
        for (int index = 0; index < MODIFIERS_NAMES.size(); index++) {
            String name = MODIFIERS_NAMES.get(index);
            String weight = MODIFIERS_WEIGHTS.get(index);
            String rarity = MODIFIERS_RARITIES.get(index);
            String attribute = MODIFIERS_ATTRIBUTES.get(index);
            String amount = MODIFIERS_AMOUNTS.get(index);
            String operations_id = MODIFIERS_OPERATIONS_IDS.get(index);
            if (attribute.contains(";")) {
                String[] attributes = attribute.split(";");
                String[] amounts = amount.split(";");
                String[] operations_ids = operations_id.split(";");
                addArmor(equipped(name).addModifiers(attributes, mods(amounts, operations_ids)).setWeight(Integer.parseInt(weight)).build());
            } else {
                Attribute entityAttribute = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(attribute));
                if (entityAttribute == null) {
                    Remodifier.LOGGER.error("Invalid value: {}", attribute);
                    return;
                }
                addArmor(equipped(name).setWeight(Integer.parseInt(weight)).setRarity(Integer.parseInt(rarity)).addModifier(entityAttribute, mod(Double.parseDouble(amount), AttributeModifier.Operation.fromValue(Integer.parseInt(operations_id)))).build());
            }
        }
    }

    private static void initCuriosModifiers() {
        List<? extends String> MODIFIERS_NAMES = merge(CuriosModifiersConfig.NAMES.get(), ModifierHandler.getCuriosNames());
        List<? extends String> MODIFIERS_WEIGHTS = merge(CuriosModifiersConfig.WEIGHTS.get(), ModifierHandler.getCuriosWeights());
        List<? extends String> MODIFIERS_RARITIES = merge(CuriosModifiersConfig.RARITIES.get(), ModifierHandler.getCuriosRarities());
        List<? extends String> MODIFIERS_ATTRIBUTES = merge(CuriosModifiersConfig.ATTRIBUTES.get(), ModifierHandler.getCuriosAttributes());
        List<? extends String> MODIFIERS_AMOUNTS = merge(CuriosModifiersConfig.AMOUNTS.get(), ModifierHandler.getCuriosAmounts());
        List<? extends String> MODIFIERS_OPERATIONS_IDS = merge(CuriosModifiersConfig.OPERATIONS_IDS.get(), ModifierHandler.getCuriosOperationsIDS());
        for (int index = 0; index < MODIFIERS_NAMES.size(); index++) {
            String name = MODIFIERS_NAMES.get(index);
            String weight = MODIFIERS_WEIGHTS.get(index);
            String rarity = MODIFIERS_RARITIES.get(index);
            String attribute = MODIFIERS_ATTRIBUTES.get(index);
            String amount = MODIFIERS_AMOUNTS.get(index);
            String operations_id = MODIFIERS_OPERATIONS_IDS.get(index);
            if (attribute.contains(";")) {
                String[] attributes = attribute.split(";");
                String[] amounts = amount.split(";");
                String[] operations_ids = operations_id.split(";");
                addCurio(equipped(name).addModifiers(attributes, mods(amounts, operations_ids)).setWeight(Integer.parseInt(weight)).build());
            } else {
                Attribute entityAttribute = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(attribute));
                if (entityAttribute == null) {
                    Remodifier.LOGGER.error("Invalid value: {}", attribute);
                    return;
                }
                addCurio(equipped(name).setWeight(Integer.parseInt(weight)).setRarity(Integer.parseInt(rarity)).addModifier(entityAttribute, mod(Double.parseDouble(amount), AttributeModifier.Operation.fromValue(Integer.parseInt(operations_id)))).build());
            }
        }
    }

    public static void initialize() {
        try {
            initToolModifiers();
            initArmorsModifiers();
            initBowModifiers();
            initShieldModifiers();
            if (Remodifier.isCuriosLoaded() && !CuriosModifiersConfig.WHETHER_OR_NOT_CURIOS_USE_ARMOR_MODIFIERS.get()) initCuriosModifiers();
        } catch (Exception e) {
            if (!ReModifierConfig.CRASHTHEGAME_IFCONFIG_HASERRORS.get()) {
                Remodifier.LOGGER.error("Exception occurs during modifiers initialization", e);
            } else {
                Remodifier.LOGGER.error("Exception occurs during modifiers initialization", e);
                Runtime.getRuntime().exit(-1);
            }
        }
    }
}
