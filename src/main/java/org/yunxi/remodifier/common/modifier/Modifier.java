package org.yunxi.remodifier.common.modifier;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.yunxi.remodifier.common.attribute.PercentageFormattable;

import java.util.List;
import java.util.UUID;

import static net.minecraft.world.item.ItemStack.ATTRIBUTE_MODIFIER_FORMAT;


public class Modifier {
    public final ResourceLocation name;
    public final String debugName;
    public final int weight;
    public final int rarity;
    public final ModifierType type;
    public final List<String> whitelist;
    public final List<String> blacklist;
    public final List<Pair<Attribute, AttributeModifierSupplier>> modifiers;

    public Modifier(ResourceLocation name, String debugName, int weight, int rarity, ModifierType type, List<Pair<Attribute, AttributeModifierSupplier>> modifiers, List<String> whitelist, List<String> blacklist) {
        this.name = name;
        this.debugName = debugName;
        this.weight = weight;
        this.rarity = rarity;
        this.type = type;
        this.modifiers = modifiers;
        this.whitelist = whitelist;
        this.blacklist = blacklist;
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
        if (entry.getKey() instanceof PercentageFormattable && modifierSupplier.operation == AttributeModifier.Operation.ADDITION){
            if (amount > 0) {
                return Component.translatable("attribute.remodifier.plus.0", ATTRIBUTE_MODIFIER_FORMAT.format(temp * 100),
                        Component.translatable(entry.getKey().getDescriptionId())).setStyle(Style.EMPTY.applyFormat(ChatFormatting.BLUE));
            } else if (amount < 0) {
                return Component.translatable("attribute.remodifier.take.0", ATTRIBUTE_MODIFIER_FORMAT.format(Math.abs(temp * 100)),
                        Component.translatable(entry.getKey().getDescriptionId())).setStyle(Style.EMPTY.applyFormat(ChatFormatting.RED));
            }
        } else {
            if (amount > 0) {
                return Component.translatable("attribute.modifier.plus." + modifierSupplier.operation.toValue(), ATTRIBUTE_MODIFIER_FORMAT.format(temp),
                        Component.translatable(entry.getKey().getDescriptionId())).setStyle(Style.EMPTY.applyFormat(ChatFormatting.BLUE));
            } else if (amount < 0) {
                return Component.translatable("attribute.modifier.take." + modifierSupplier.operation.toValue(), ATTRIBUTE_MODIFIER_FORMAT.format(Math.abs(temp)),
                        Component.translatable(entry.getKey().getDescriptionId())).setStyle(Style.EMPTY.applyFormat(ChatFormatting.RED));
            }
        }

        return null;
    }

    public List<MutableComponent> getInfoLines() {
        List<MutableComponent> lines = new ObjectArrayList<>();
        int size = modifiers.size();
        if (size < 1) return lines;
        if (!I18n.get("modifier." + name.getNamespace() + "." + name.getPath() + ".desc").equalsIgnoreCase("modifier." + name.getNamespace() + "." + name.getPath() + ".desc")) {
            lines.add(getFormattedName().copy().append(": ").setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)).append(Component.translatable("modifier." + name.getNamespace() + "." + name.getPath() + ".desc")));
        } else {
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
        }
        return lines;
    }

    @SuppressWarnings("removal")
    public static class ModifierBuilder {
        int weight = 100;
        int rarity = 1;
        final ResourceLocation name;
        final String debugName;
        final ModifierType type;
        List<Pair<Attribute, AttributeModifierSupplier>> modifiers = new ObjectArrayList<>();
        List<String> whitelist = null;
        List<String> blacklist = null;

        public ModifierBuilder(ResourceLocation name, String debugName, ModifierType type) {
            this.name = name;
            this.debugName = debugName;
            this.type = type;
        }

        public ModifierBuilder setWeight(int weight) {
            this.weight = Math.max(0, weight);
            return this;
        }

        public ModifierBuilder setRarity(int rarity) {
            this.rarity = Math.max(1, rarity);
            return this;
        }

        public ModifierBuilder addModifier(Attribute attribute, AttributeModifierSupplier modifier) {
            modifiers.add(new ImmutablePair<>(attribute, modifier));
            return this;
        }

        public ModifierBuilder setWhitelist(List<String> whitelist) {
            this.whitelist = whitelist;
            return this;
        }

        public ModifierBuilder setBlacklist(List<String> blacklist) {
            this.blacklist = blacklist;
            return this;
        }

        public ModifierBuilder addModifiers(String @NotNull [] attribute, AttributeModifierSupplier[] modifier) {
            for (int index = 0; index < attribute.length; index++) {
                String entityAttribute = attribute[index];
                Attribute registryAttribute = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(entityAttribute));
                if (registryAttribute == null) {
                    throw new RuntimeException("Invalid key: " + entityAttribute);
                }
                modifiers.add(new ImmutablePair<>(registryAttribute, modifier[index]));
            }
            return this;
        }

        public Modifier build() {
            return new Modifier(name, debugName, weight,rarity, type, modifiers, whitelist, blacklist);
        }
    }


    public record AttributeModifierSupplier(double amount, AttributeModifier.Operation operation) {
        @Contract(value = "_, _ -> new", pure = true)
        public @NotNull AttributeModifier getAttributeModifier(UUID id, String name) {
            return new AttributeModifier(id, name, amount, operation);
        }
    }
}
