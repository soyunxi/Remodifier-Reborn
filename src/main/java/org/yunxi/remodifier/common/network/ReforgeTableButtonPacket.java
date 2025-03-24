package org.yunxi.remodifier.common.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import org.yunxi.remodifier.client.ReforgeTableContainer;
import org.yunxi.remodifier.common.block.ReforgedTableBlockEntity;

import java.util.function.Supplier;

public class ReforgeTableButtonPacket {
    private final BlockPos pos;

    public ReforgeTableButtonPacket(BlockPos pos) {
        this.pos = pos;
    }

    public static ReforgeTableButtonPacket decode(FriendlyByteBuf buf) {
        return new ReforgeTableButtonPacket(buf.readBlockPos());
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            ServerPlayer player = contextSupplier.get().getSender();
            if (player != null) {
                Level level = player.level();
                if (level.getBlockEntity(pos) instanceof ReforgedTableBlockEntity container) {

                }
            }
        });
        contextSupplier.get().setPacketHandled(true);
    }
}
