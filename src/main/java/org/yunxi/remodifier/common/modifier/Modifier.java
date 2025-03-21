package org.yunxi.remodifier.common.modifier;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import javafx.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

import static net.minecraft.world.item.ItemStack.ATTRIBUTE_MODIFIER_FORMAT;


public class Modifier {
    public final ResourceLocation name;
    public final String debugName;
    public final int weight;
    public final int rarity;
    public final ModifierType type;
    public final List<Pair<Attribute, AttributeModifierSupplier>> modifiers;

    public Modifier(ResourceLocation name, String debugName, int weight, int rarity, ModifierType type, List<Pair<Attribute, AttributeModifierSupplier>> modifiers) {
        this.name = name;
        this.debugName = debugName;
        this.weight = weight;
        this.rarity = rarity;
        this.type = type;
        this.modifiers = modifiers;
    }

    public Component getFormattedName() {
        return Component.translatable("modifier." + name.getNamespace() + "." + name.getPath());
    }

    public static MutableComponent getModifierDescription(Pair<Attribute, AttributeModifierSupplier> entry) {
        AttributeModifierSupplier modifierSupplier = entry.getValue();
        double amount = modifierSupplier.amount;

        double temp;
        if (modifierSupplier.operation == AttributeModifier.Operation.ADDITION) {
            if (entry.getKey().equals(Attributes.KNOCKBACK_RESISTANCE)) {
                temp = amount * 10d;
            } else {
                temp = amount;
            }
        } else {
            temp = amount * 100d;
        }

        if (amount > 0) {
            return Component.translatable("attribute.modifier.plus." + modifierSupplier.operation.toValue(), ATTRIBUTE_MODIFIER_FORMAT.format(temp),
                    entry.getKey().getDescriptionId().formatted(ChatFormatting.BLUE));
        } else if (amount < 0) {
            return Component.translatable("attribute.modifier.take." + modifierSupplier.operation.toValue(), ATTRIBUTE_MODIFIER_FORMAT.format(temp),
                    entry.getKey().getDescriptionId().formatted(ChatFormatting.RED));
        }

        return null;
    }

    public List<MutableComponent> getInfoLines() {
        List<MutableComponent> lines = new ObjectArrayList<>();
        int size = modifiers.size();
        if (size < 1) return lines;
        if (size == 1) {
            MutableComponent description = getModifierDescription(modifiers.get(0));
            if (description == null) return lines;
            lines.add(getFormattedName().copy().append(": ").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)).append(description));
        } else {
            lines.add(getFormattedName().copy().append(": ").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
            for (Pair<Attribute, AttributeModifierSupplier> modifier : modifiers) {
                MutableComponent modifierDescription = getModifierDescription(modifier);
                if (modifierDescription != null) lines.add(modifierDescription);
            }
            if (lines.size() == 1) lines.clear();
        }
        return lines;
    }

    public record AttributeModifierSupplier(double amount, AttributeModifier.Operation operation) {

        @Contract(value = "_, _ -> new", pure = true)
        public @NotNull AttributeModifier getAttributeModifier(UUID id, String name) {
            return new AttributeModifier(id, name, amount, operation);
        }
    }
}
