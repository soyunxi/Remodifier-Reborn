package org.yunxi.remodifier.common.item;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.yunxi.remodifier.Remodifier;
import org.yunxi.remodifier.common.modifier.Modifier;
import org.yunxi.remodifier.common.modifier.ModifierHandler;
import org.yunxi.remodifier.common.modifier.Modifiers;

import java.util.List;

public class ModifierBookItem extends Item {
    public ModifierBookItem() {
        super(new Item.Properties().rarity(Rarity.EPIC));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        if (!stack.hasTag()) return false;
        CompoundTag tag = stack.getTag();
        if (tag == null) return false;
        return tag.contains(ModifierHandler.bookTagName) && !tag.getString(ModifierHandler.bookTagName).equals("modifiers:none");

    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public Component getName(ItemStack stack) {
        Component base = super.getName(stack);
        if (!stack.hasTag() || (stack.getTag() != null && !stack.getTag().contains(ModifierHandler.bookTagName))) return base;
        Modifier mod = Modifiers.MODIFIERS.get(ResourceLocation.parse(stack.getTag().getString(ModifierHandler.bookTagName)));
        if (mod == null) return base;
        return Component.translatable("misc.modifiers.modifier_prefix").append(mod.getFormattedName());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level p_41422_, List<Component> tooltip, TooltipFlag p_41424_) {
        String translationKey = this.getDescriptionId();
        if (stack.getTag() != null && stack.getTag().contains(ModifierHandler.bookTagName)) {
            Modifier mod = Modifiers.MODIFIERS.get(ResourceLocation.parse(stack.getTag().getString(ModifierHandler.bookTagName)));
            if (mod != null) {
                tooltip.addAll(mod.getInfoLines());
                tooltip.add(Component.translatable(translationKey + ".tooltip.0"));
                tooltip.add(Component.translatable(translationKey + ".tooltip.1"));
                return;
            }
        }
        tooltip.add(Component.translatable(translationKey + ".tooltip.invalid"));
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        List<String> toolNames = ModifierHandler.getToolNames();
        for (String toolName : toolNames) {
            player.displayClientMessage(Component.translatable(toolName), false);
        }
        return super.onLeftClickEntity(stack, player, entity);
    }

    public static List<ItemStack> getStacks() {
        List<Modifier> modifiers = new ObjectArrayList<>();
        modifiers.add(Modifiers.NONE);
        modifiers.addAll(Modifiers.curioPool.modifiers);
        modifiers.addAll(Modifiers.toolPool.modifiers);
        modifiers.addAll(Modifiers.shieldPool.modifiers);
        modifiers.addAll(Modifiers.bowPool.modifiers);
        modifiers.addAll(Modifiers.armorPool.modifiers);

        List<ItemStack> stacks = new ObjectArrayList<>();
        for (Modifier mod : modifiers) {
            ItemStack stack = new ItemStack(Remodifier.MODIFIER_BOOK.get());
            stack.getOrCreateTag().putString(ModifierHandler.bookTagName, mod.name.toString());
            stacks.add(stack);
        }
        return stacks;
    }
}
