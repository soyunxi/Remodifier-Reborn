package org.yunxi.remodifier.common.modifier;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yunxi.remodifier.common.config.JsonConfigInitialier;
import org.yunxi.remodifier.common.config.toml.modifiers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@SuppressWarnings("removal")
public class ModifierHandler {
    public static final long COMMON_SEGMENT_CURIO = (0x7a6ca76cL) << 32;
    public static final long COMMON_SEGMENT_EQUIPMENT = 0x9225d5c4fd8d434bL;
    public static final String tagName = "itemModifier";
    public static final String bookTagName = "bookModifier";

    public static boolean canHaveModifiers(@NotNull ItemStack stack) {
        return Modifiers.curioPool.isApplicable.test(stack) || Modifiers.toolPool.isApplicable.test(stack) || Modifiers.bowPool.isApplicable.test(stack) || Modifiers.shieldPool.isApplicable.test(stack) || Modifiers.armorPool.isApplicable.test(stack) || Modifiers.weaponPool.isApplicable.test(stack);
    }

    @Nullable
    public static Modifier rollModifier(ItemStack stack, Random random) {
        if (!canHaveModifiers(stack)) return null;
        if (Modifiers.curioPool.isApplicable.test(stack)) return Modifiers.curioPool.roll(stack, random);
        if (Modifiers.toolPool.isApplicable.test(stack)) return Modifiers.toolPool.roll(stack, random);
        if (Modifiers.bowPool.isApplicable.test(stack)) return Modifiers.bowPool.roll(stack, random);
        if (Modifiers.shieldPool.isApplicable.test(stack)) return Modifiers.shieldPool.roll(stack, random);
        if (Modifiers.armorPool.isApplicable.test(stack)) return Modifiers.armorPool.roll(stack, random);
        if (Modifiers.weaponPool.isApplicable.test(stack)) return Modifiers.weaponPool.roll(stack, random);
        return null;
    }

    public static void setModifier(ItemStack stack, Modifier modifier) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.remove(tagName);
        tag.putString(tagName, modifier.name.toString());
    }

    public static boolean hasModifier(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.contains(tagName);
    }

    @Nullable
    public static Modifier getModifier(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null) return null;
        if (!tag.contains(tagName)) return null;
        return Modifiers.MODIFIERS.get(new ResourceLocation(tag.getString(tagName)));
    }

    public static UUID getCurioUuid(String identifier, int slot, int attributeIndex) {
        long second = (((long) attributeIndex)<<32) | ((long) identifier.hashCode());
        long first = COMMON_SEGMENT_CURIO | (((long) slot) << 32);
        return new UUID(first, second);
    }

    public static void applyCurioModifier(LivingEntity entity, Modifier modifier, String slotIdentifier, int index) {
        if (modifier.type == ModifierType.HELD) {
            return;
        }
        for (int i = 0; i < modifier.modifiers.size(); i++) {
            Pair<Attribute, Modifier.AttributeModifierSupplier> entry = modifier.modifiers.get(i);
            UUID id = getCurioUuid(slotIdentifier, index, i);
            AttributeInstance instance = entity.getAttribute(entry.getKey());
            if (instance != null && instance.getModifier(id) == null) {
                instance.addTransientModifier(entry.getValue().getAttributeModifier(id, "curio_modifier_"+modifier.debugName));
            }
        }
    }

    public static void removeCurioModifier(LivingEntity entity, Modifier modifier, String slotIdentifier, int index) {
        for (int i = 0; i < modifier.modifiers.size(); i++) {
            Pair<Attribute, Modifier.AttributeModifierSupplier> entry = modifier.modifiers.get(i);
            UUID id = getCurioUuid(slotIdentifier, index, i);
            AttributeInstance instance = entity.getAttribute(entry.getKey());
            if (instance != null) {
                instance.removeModifier(id);
            }
        }
    }

    public static UUID getEquipmentUuid(EquipmentSlot slot, int attributeIndex) {
        long second = (((long) attributeIndex)<<32) | ((long) slot.hashCode());
        return new UUID(COMMON_SEGMENT_EQUIPMENT, second);
    }

    public static void applyEquipmentModifier(LivingEntity entity, @NotNull Modifier modifier, EquipmentSlot type) {
        if (modifier.type == ModifierType.HELD && type.getType() == EquipmentSlot.Type.ARMOR || modifier.type == ModifierType.EQUIPPED && type.getType() == EquipmentSlot.Type.HAND) {
            return;
        }
        for (int i = 0; i < modifier.modifiers.size(); i++) {
            Pair<Attribute, Modifier.AttributeModifierSupplier> entry = modifier.modifiers.get(i);
            UUID id = getEquipmentUuid(type, i);
            AttributeInstance instance = entity.getAttribute(entry.getKey());
            if (instance != null && instance.getModifier(id) == null) {
                instance.addTransientModifier(entry.getValue().getAttributeModifier(id, "equipment_modifier_" + modifier.debugName));
            }
        }
    }

    public static void removeEquipmentModifier(LivingEntity entity, Modifier modifier, EquipmentSlot type) {
        for (int i = 0; i < modifier.modifiers.size(); i++) {
            Pair<Attribute, Modifier.AttributeModifierSupplier> entry = modifier.modifiers.get(i);
            UUID id = getEquipmentUuid(type, i);
            AttributeInstance instance = entity.getAttribute(entry.getKey());
            if (instance != null) {
                instance.removeModifier(id);
            }
        }
    }

    @NotNull
    private static List<Integer> getTypeModifier(String modifierType) {
        List<Integer> integers = new ArrayList<>();
        List<String> modifierTypes = JsonConfigInitialier.MODIFIER_TYPES;
        for (int i = 0; i < modifierTypes.size(); i++) {
            if (modifierTypes.get(i).equals(modifierType)) integers.add(i);
        }
        return integers;
    }

    public static List<String> getBowNames() {
        List<String> list = new ArrayList<>();
        List<Integer> integers = getTypeModifier("bow");
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_NAMES.get(integer));
        }
        return list;
    }

    public static List<String> getBowWeights() {
        List<String> list = new ArrayList<String>();
        List<Integer> integers = getTypeModifier("bow");
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_WEIGHTS.get(integer));
        }
        return list;
    }

    public static List<String> getBowRarities() {
        List<String> list = new ArrayList<String>();
        List<Integer> integers = getTypeModifier("bow");
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_RARITY.get(integer));
        }
        return list;
    }

    public static List<String> getBowAttributes() {
        List<String> list = new ArrayList<String>();
        List<Integer> integers = getTypeModifier("bow");
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_ATTRIBUTES.get(integer));
        }
        return list;
    }

    public static List<String> getBowAmounts() {
        List<String> list = new ArrayList<String>();
        List<Integer> integers = getTypeModifier("bow");
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_AMOUNTS.get(integer));
        }
        return list;
    }

    public static List<String> getBowOperationsIDS() {
        List<String> list = new ArrayList<String>();
        List<Integer> integers = getTypeModifier("bow");
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_OPERATION_ID.get(integer));
        }
        return list;
    }

    public static List<List<String>> getBowWhitelist() {
        List<List<String>> list = new ArrayList<>();
        List<Integer> integers = getTypeModifier("bow");
        for (String s : BowModifiersConfig.NAMES.get()) {
            list.add(null);
        }
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_WHITE_LIST.get(integer));
        }
        return list;
    }

    public static List<List<String>> getBowBlacklist() {
        List<List<String>> list = new ArrayList<>();
        List<Integer> integers = getTypeModifier("bow");
        for (String s : BowModifiersConfig.NAMES.get()) {
            list.add(null);
        }
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_WHITE_LIST.get(integer));
        }
        return list;
    }

    public static List<String> getShieldNames() {
        List<String> list = new ArrayList<>();
        List<Integer> integers = getTypeModifier("shield");
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_NAMES.get(integer));
        }
        return list;
    }

    public static List<String> getShieldWeights() {
        List<String> list = new ArrayList<String>();
        List<Integer> integers = getTypeModifier("shield");
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_WEIGHTS.get(integer));
        }
        return list;
    }

    public static List<String> getShieldRarities() {
        List<String> list = new ArrayList<String>();
        List<Integer> integers = getTypeModifier("shield");
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_RARITY.get(integer));
        }
        return list;
    }

    public static List<String> getShieldAttributes() {
        List<String> list = new ArrayList<String>();
        List<Integer> integers = getTypeModifier("shield");
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_ATTRIBUTES.get(integer));
        }
        return list;
    }

    public static List<String> getShieldAmounts() {
        List<String> list = new ArrayList<String>();
        List<Integer> integers = getTypeModifier("shield");
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_AMOUNTS.get(integer));
        }
        return list;
    }

    public static List<String> getShieldOperationsIDS() {
        List<String> list = new ArrayList<String>();
        List<Integer> integers = getTypeModifier("shield");
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_OPERATION_ID.get(integer));
        }
        return list;
    }

    public static List<List<String>> getShieldWhitelist() {
        List<List<String>> list = new ArrayList<>();
        List<Integer> integers = getTypeModifier("shield");
        for (String s : ShieldModifiersConfig.NAMES.get()) {
            list.add(null);
        }
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_WHITE_LIST.get(integer));
        }
        return list;
    }

    public static List<List<String>> getShieldBlacklist() {
        List<List<String>> list = new ArrayList<>();
        List<Integer> integers = getTypeModifier("shield");
        for (String s : ShieldModifiersConfig.NAMES.get()) {
            list.add(null);
        }
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_WHITE_LIST.get(integer));
        }
        return list;
    }

    public static List<String> getToolNames() {
        List<String> list = new ArrayList<>();
        List<Integer> integers = getTypeModifier("tool");
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_NAMES.get(integer));
        }
        return list;
    }

    public static List<String> getToolWeights() {
        List<String> list = new ArrayList<String>();
        List<Integer> integers = getTypeModifier("tool");
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_WEIGHTS.get(integer));
        }
        return list;
    }

    public static List<String> getToolRarities() {
        List<String> list = new ArrayList<String>();
        List<Integer> integers = getTypeModifier("tool");
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_RARITY.get(integer));
        }
        return list;
    }

    public static List<String> getToolAttributes() {
        List<String> list = new ArrayList<String>();
        List<Integer> integers = getTypeModifier("tool");
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_ATTRIBUTES.get(integer));
        }
        return list;
    }

    public static List<String> getToolAmounts() {
        List<String> list = new ArrayList<String>();
        List<Integer> integers = getTypeModifier("tool");
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_AMOUNTS.get(integer));
        }
        return list;
    }

    public static List<String> getToolOperationsIDS() {
        List<String> list = new ArrayList<String>();
        List<Integer> integers = getTypeModifier("tool");
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_OPERATION_ID.get(integer));
        }
        return list;
    }

    public static List<List<String>> getToolWhitelist() {
        List<List<String>> list = new ArrayList<>();
        List<Integer> integers = getTypeModifier("tool");
        for (String s : ToolModifiersConfig.NAMES.get()) {
            list.add(null);
        }
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_WHITE_LIST.get(integer));
        }
        return list;
    }

    public static List<List<String>> getToolBlacklist() {
        List<List<String>> list = new ArrayList<>();
        List<Integer> integers = getTypeModifier("tool");
        for (String s : ToolModifiersConfig.NAMES.get()) {
            list.add(null);
        }
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_WHITE_LIST.get(integer));
        }
        return list;
    }

    public static List<String> getWeaponNames() {
        List<String> list = new ArrayList<>();
        List<Integer> integers = getTypeModifier("weapon");
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_NAMES.get(integer));
        }
        return list;
    }

    public static List<String> getWeaponWeights() {
        List<String> list = new ArrayList<String>();
        List<Integer> integers = getTypeModifier("weapon");
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_WEIGHTS.get(integer));
        }
        return list;
    }

    public static List<String> getWeaponRarities() {
        List<String> list = new ArrayList<String>();
        List<Integer> integers = getTypeModifier("weapon");
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_RARITY.get(integer));
        }
        return list;
    }

    public static List<String> getWeaponAttributes() {
        List<String> list = new ArrayList<String>();
        List<Integer> integers = getTypeModifier("weapon");
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_ATTRIBUTES.get(integer));
        }
        return list;
    }

    public static List<String> getWeaponAmounts() {
        List<String> list = new ArrayList<String>();
        List<Integer> integers = getTypeModifier("weapon");
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_AMOUNTS.get(integer));
        }
        return list;
    }

    public static List<String> getWeaponOperationsIDS() {
        List<String> list = new ArrayList<String>();
        List<Integer> integers = getTypeModifier("weapon");
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_OPERATION_ID.get(integer));
        }
        return list;
    }

    public static List<List<String>> getWeaponWhitelist() {
        List<List<String>> list = new ArrayList<>();
        List<Integer> integers = getTypeModifier("weapon");
        for (String s : WeaponModifiersConfig.NAMES.get()) {
            list.add(null);
        }
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_WHITE_LIST.get(integer));
        }
        return list;
    }

    public static List<List<String>> getWeaponBlacklist() {
        List<List<String>> list = new ArrayList<>();
        List<Integer> integers = getTypeModifier("weapon");
        for (String s : WeaponModifiersConfig.NAMES.get()) {
            list.add(null);
        }
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_WHITE_LIST.get(integer));
        }
        return list;
    }

    public static List<String> getArmorsNames() {
        List<String> list = new ArrayList<>();
        List<Integer> integers = getTypeModifier("armor");
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_NAMES.get(integer));
        }
        return list;
    }

    public static List<String> getArmorsWeights() {
        List<String> list = new ArrayList<String>();
        List<Integer> integers = getTypeModifier("armor");
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_WEIGHTS.get(integer));
        }
        return list;
    }

    public static List<String> getArmorsRarities() {
        List<String> list = new ArrayList<String>();
        List<Integer> integers = getTypeModifier("armor");
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_RARITY.get(integer));
        }
        return list;
    }

    public static List<String> getArmorsAttributes() {
        List<String> list = new ArrayList<String>();
        List<Integer> integers = getTypeModifier("armor");
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_ATTRIBUTES.get(integer));
        }
        return list;
    }

    public static List<String> getArmorsAmounts() {
        List<String> list = new ArrayList<String>();
        List<Integer> integers = getTypeModifier("armor");
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_AMOUNTS.get(integer));
        }
        return list;
    }

    public static List<String> getArmorsOperationsIDS() {
        List<String> list = new ArrayList<String>();
        List<Integer> integers = getTypeModifier("armor");
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_OPERATION_ID.get(integer));
        }
        return list;
    }

    public static List<List<String>> getArmorsWhitelist() {
        List<List<String>> list = new ArrayList<>();
        List<Integer> integers = getTypeModifier("armor");
        for (String s : ArmorModifiersConfig.NAMES.get()) {
            list.add(null);
        }
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_WHITE_LIST.get(integer));
        }
        return list;
    }

    public static List<List<String>> getArmorsBlacklist() {
        List<List<String>> list = new ArrayList<>();
        List<Integer> integers = getTypeModifier("armor");
        for (String s : ArmorModifiersConfig.NAMES.get()) {
            list.add(null);
        }
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_WHITE_LIST.get(integer));
        }
        return list;
    }

    public static List<String> getCuriosNames() {
        List<String> list = new ArrayList<>();
        List<Integer> integers = getTypeModifier("curios");
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_NAMES.get(integer));
        }
        return list;
    }

    public static List<String> getCuriosWeights() {
        List<String> list = new ArrayList<String>();
        List<Integer> integers = getTypeModifier("curios");
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_WEIGHTS.get(integer));
        }
        return list;
    }

    public static List<String> getCuriosRarities() {
        List<String> list = new ArrayList<String>();
        List<Integer> integers = getTypeModifier("curios");
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_RARITY.get(integer));
        }
        return list;
    }

    public static List<String> getCuriosAttributes() {
        List<String> list = new ArrayList<String>();
        List<Integer> integers = getTypeModifier("curios");
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_ATTRIBUTES.get(integer));
        }
        return list;
    }

    public static List<String> getCuriosAmounts() {
        List<String> list = new ArrayList<String>();
        List<Integer> integers = getTypeModifier("curios");
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_AMOUNTS.get(integer));
        }
        return list;
    }

    public static List<String> getCuriosOperationsIDS() {
        List<String> list = new ArrayList<String>();
        List<Integer> integers = getTypeModifier("curios");
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_OPERATION_ID.get(integer));
        }
        return list;
    }

    public static List<List<String>> getCuriosWhitelist() {
        List<List<String>> list = new ArrayList<>();
        List<Integer> integers = getTypeModifier("curios");
        for (String s : CuriosModifiersConfig.NAMES.get()) {
            list.add(null);
        }
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_WHITE_LIST.get(integer));
        }
        return list;
    }

    public static List<List<String>> getCuriosBlacklist() {
        List<List<String>> list = new ArrayList<>();
        List<Integer> integers = getTypeModifier("curios");
        for (String s : CuriosModifiersConfig.NAMES.get()) {
            list.add(null);
        }
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_WHITE_LIST.get(integer));
        }
        return list;
    }
}
