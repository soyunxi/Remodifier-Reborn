package org.yunxi.remodifier.client;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
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
        if (slot != null && slot.hasItem()) {
            ItemStack sourceStack = slot.getItem();
            itemstack = sourceStack.copy();

            // 容器槽位 -> 玩家背包/快捷栏
            if (index < 2) {
                boolean moved = this.moveItemStackTo(sourceStack, 2, this.slots.size(), true);
                if (!moved) {
                    return ItemStack.EMPTY; // 完全无法移动
                }
            }
            // 玩家背包/快捷栏 -> 容器槽位
            else {
                // 1. 先尝试移动到容器槽位
                boolean movedToContainer = this.moveItemStackTo(sourceStack, 0, 2, false);
                if (!movedToContainer) {
                    // 2. 容器移动失败后，处理玩家内部移动
                    if (index >= 2 && index < 29) { // 主背包 -> 快捷栏
                        movedToContainer = this.moveItemStackTo(sourceStack, 29, 38, false);
                    } else if (index >= 29 && index < 38) { // 快捷栏 -> 主背包
                        movedToContainer = this.moveItemStackTo(sourceStack, 2, 29, false);
                    }
                    if (!movedToContainer) {
                        return ItemStack.EMPTY; // 完全无法移动
                    }
                }
            }

            // 更新原槽位状态
            if (sourceStack.isEmpty()) {
                slot.set(ItemStack.EMPTY); // 完全移出物品时清空槽位
            } else {
                slot.setChanged(); // 部分移动时标记更新
            }

            // 检查是否未移动任何物品
            if (sourceStack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            // 触发槽位更新事件
            slot.onTake(player, sourceStack);
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
