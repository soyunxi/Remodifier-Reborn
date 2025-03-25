package org.yunxi.remodifier.common.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import org.yunxi.remodifier.common.block.ReforgedTableBlockEntity;

import java.util.function.Supplier;

public class ReforgeTableButtonPacket {
    private final BlockPos pos;

    public ReforgeTableButtonPacket(BlockPos pos) {
        this.pos = pos;
    }

    public static void encode(ReforgeTableButtonPacket packet, FriendlyByteBuf buf) {
        buf.writeBlockPos(packet.pos);
    }

    public static ReforgeTableButtonPacket decode(FriendlyByteBuf buf) {
        return new ReforgeTableButtonPacket(buf.readBlockPos());
    }

    public static void handle(ReforgeTableButtonPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            if (player != null) {
                Level level = player.level();
                if (level.getBlockEntity(packet.pos) instanceof ReforgedTableBlockEntity be) {
                    be.startProcessing();
                }
            }
        });
        context.get().setPacketHandled(true);
    }
}
