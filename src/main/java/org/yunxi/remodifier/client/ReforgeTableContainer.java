package org.yunxi.remodifier.client;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import org.yunxi.remodifier.Remodifier;
import org.yunxi.remodifier.common.block.ReforgedTableBlockEntity;

public class ReforgeTableContainer extends AbstractContainerMenu {
    private final BlockPos pos;

    private final Player player;

    public ReforgeTableContainer(int pContainerId, Player player, BlockPos pos) {
        super(Remodifier.REFORGED_TABLE_MENU.get(), pContainerId);
        this.pos = pos;
        this.player = player;
        if (player.level().getBlockEntity(pos) instanceof ReforgedTableBlockEntity reforgedTableBlockEntity) {
            addSlot(new SlotItemHandler(reforgedTableBlockEntity.getIItemStackHandler(), 0, 27, 47));
            addSlot(new SlotItemHandler(reforgedTableBlockEntity.getIItemStackHandler(), 1, 76, 47));
        }
        layoutPlayerInventorySlots(player.getInventory(), 8, 84);
    }

    public ReforgedTableBlockEntity getReforgedTableBlockEntity() {
        return (ReforgedTableBlockEntity) player.level().getBlockEntity(pos);
    }

    public BlockPos getPos() {
        return pos;
    }

    public Player getPlayer() {
        return player;
    }

    private int addSlotRange(Container playerInventory, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            addSlot(new Slot(playerInventory, index, x, y));
            x += dx; // 横向间隔
            index++;
        }
        return index;
    }


    private int addSlotBox(Container playerInventory, int index, int x, int y,
                           int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
            index = addSlotRange(playerInventory, index, x, y, horAmount, dx);
            y += dy; // 纵向间隔
        }
        return index;
    }


    private void layoutPlayerInventorySlots(Container playerInventory, int leftCol, int topRow) {
        // 玩家背包主区域（3行9列）
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);
        // 快捷栏（1行9列）
        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }


    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack item = slot.getItem();
            itemstack = item.copy();
            if (index < 2) {
                if (moveItemStackTo(itemstack, 2, slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!moveItemStackTo(item, 0, 2, false)) {
                    if (index < 2 + 27) {
                        if (!moveItemStackTo(itemstack, 2 +  27, 2 + 36, false)) {
                            return ItemStack.EMPTY;
                        }
                    }

                    else if (index < 2 + Inventory.INVENTORY_SIZE) {
                        if (!moveItemStackTo(itemstack, 2, 2 + 27, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                }
                if (item.isEmpty()) {
                    slot.set(ItemStack.EMPTY);
                } else {
                    slot.setChanged();
                }
                if (item.getCount() == itemstack.getCount()) {
                    return ItemStack.EMPTY;
                }
                slot.onTake(player, item);
            }
        }
        return itemstack;
    }


    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(player.level(), pos),
                player,
                Remodifier.REFORGED_TABLE.get());
    }
}
