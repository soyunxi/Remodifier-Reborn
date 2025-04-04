package org.yunxi.remodifier.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import org.yunxi.remodifier.Remodifier;
import org.yunxi.remodifier.client.ReforgeTableContainer;
import org.yunxi.remodifier.common.config.toml.ReforgeConfig;
import org.yunxi.remodifier.common.modifier.Modifier;
import org.yunxi.remodifier.common.modifier.ModifierHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("removal")
public class ReforgedTableBlockEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler itemStackHandler = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };
    private LazyOptional<IItemHandler> rollItemHandler = LazyOptional.of(() -> new CombinedInvWrapper(itemStackHandler));
    public static final String REFORGED_TABLE_BLOCK_ENTITY = "screen.remodifier.reforged_table";

    public ReforgedTableBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(Remodifier.REFORGED_TABLE_TYPE.get(), pPos, pBlockState);
    }

    public ItemStackHandler getIItemStackHandler() {
        return itemStackHandler;
    }



    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("itemStackHandler", itemStackHandler.serializeNBT());
    }
    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains("itemStackHandler")) { // 键名已修复
            itemStackHandler.deserializeNBT(pTag.getCompound("itemStackHandler"));
        }
    }

        public void startProcessing() {
        ItemStack rollItem = itemStackHandler.getStackInSlot(0).copy();
        ItemStack reforgeItem = itemStackHandler.getStackInSlot(1).copy();
        if (canProcess()) {
            Modifier modifier = ModifierHandler.rollModifier(rollItem, ThreadLocalRandom.current());
            if (modifier != null) {
                ModifierHandler.setModifier(rollItem, modifier);
                if (ModifierHandler.hasModifier(rollItem)) {
                    reforgeItem.shrink(1);
                    itemStackHandler.setStackInSlot(0, rollItem);
                    itemStackHandler.setStackInSlot(1, reforgeItem);
                }
            }
            setChanged();
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }

    }

    public boolean canProcess() {
        ItemStack rollItem = itemStackHandler.getStackInSlot(0);
        ItemStack reforgeItem = itemStackHandler.getStackInSlot(1);
        if (rollItem.isEmpty() || reforgeItem.isEmpty()) {
            return false;
        }
        if (ModifierHandler.canHaveModifiers(reforgeItem)) return false;
        if (!ReforgeConfig.DISABLE_REPAIR_REFORGED.get()) {
            if (rollItem.getItem().isValidRepairItem(rollItem, reforgeItem)) return true;
        }
        List<Item> reforgeItems = new ArrayList<>();
        for (String s : ReforgeConfig.UNIVERSAL_REFORGE_ITEM.get()) {
            ResourceLocation parse = new ResourceLocation(s);
            Item value = ForgeRegistries.ITEMS.getValue(parse);
            if (value != null) {
                reforgeItems.add(value);
            }
        }
        return reforgeItems.contains(reforgeItem.getItem());
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(REFORGED_TABLE_BLOCK_ENTITY);
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new ReforgeTableContainer(i, player, getBlockPos());
    }
}
