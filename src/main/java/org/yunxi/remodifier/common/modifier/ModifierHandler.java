package org.yunxi.remodifier.common.modifier;

import org.jetbrains.annotations.NotNull;
import org.yunxi.remodifier.common.config.JsonConfigInitialier;

import java.util.ArrayList;
import java.util.List;

public class ModifierHandler {

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

    public static List<String> getArmorsNames() {
        List<String> list = new ArrayList<>();
        List<Integer> integers = getTypeModifier("armors");
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_NAMES.get(integer));
        }
        return list;
    }

    public static List<String> getArmorsWeights() {
        List<String> list = new ArrayList<String>();
        List<Integer> integers = getTypeModifier("armors");
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_WEIGHTS.get(integer));
        }
        return list;
    }

    public static List<String> getArmorsRarities() {
        List<String> list = new ArrayList<String>();
        List<Integer> integers = getTypeModifier("armors");
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_RARITY.get(integer));
        }
        return list;
    }

    public static List<String> getArmorsAttributes() {
        List<String> list = new ArrayList<String>();
        List<Integer> integers = getTypeModifier("armors");
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_ATTRIBUTES.get(integer));
        }
        return list;
    }

    public static List<String> getArmorsAmounts() {
        List<String> list = new ArrayList<String>();
        List<Integer> integers = getTypeModifier("armors");
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_AMOUNTS.get(integer));
        }
        return list;
    }

    public static List<String> getArmorsOperationsIDS() {
        List<String> list = new ArrayList<String>();
        List<Integer> integers = getTypeModifier("armors");
        for (Integer integer : integers) {
            list.add(JsonConfigInitialier.MODIFIER_OPERATION_ID.get(integer));
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
}
