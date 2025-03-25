package org.yunxi.remodifier.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;
import org.yunxi.remodifier.Remodifier;
import org.yunxi.remodifier.client.ReforgeTableContainer;

import java.util.List;

public class ReforgedTableBlock extends Block implements EntityBlock {
    public ReforgedTableBlock() {
        super(Properties.copy(Blocks.SMITHING_TABLE));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ReforgedTableBlockEntity(blockPos, blockState);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide) { // 只在服务端处理容器打开
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof ReforgedTableBlockEntity) {
                MenuProvider containerProvider = new MenuProvider() {
                    @Override
                    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
                        return new ReforgeTableContainer(i, player, pPos);
                    }

                    @Override
                    public Component getDisplayName() {
                        return Component.translatable(ReforgedTableBlockEntity.REFORGED_TABLE_BLOCK_ENTITY);
                    }
                };
                NetworkHooks.openScreen((ServerPlayer) pPlayer, containerProvider, blockEntity.getBlockPos());
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        List<ItemStack> drops = super.getDrops(state, params);
        drops.add(new ItemStack(Remodifier.REFORGED_TABLE.get()));
        return drops;
    }

    @Override
    public void onRemove(BlockState oldState, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!oldState.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof ReforgedTableBlockEntity tableEntity) {
                ItemStackHandler handler = tableEntity.getIItemStackHandler();
                for (int i = 0; i < handler.getSlots(); i++) {
                    ItemStack stack = handler.getStackInSlot(i);
                    if (!stack.isEmpty()) {
                        Containers.dropItemStack(
                                level,
                                pos.getX(),
                                pos.getY(),
                                pos.getZ(),
                                stack
                        );
                    }
                }
                level.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(oldState, level, pos, newState, isMoving);
        }
    }

}
