package org.yunxi.remodifier.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import org.yunxi.remodifier.common.reforge.SmithingScreenHandlerReforge;

import java.util.function.Supplier;


public class PacketC2SReforge {
    private int containerId; // 示例字段

    public PacketC2SReforge(int containerId) {
        this.containerId = containerId;
    }

    public PacketC2SReforge(FriendlyByteBuf buf) {
        this.containerId = buf.readVarInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(containerId);
    }

    public static void handle(PacketC2SReforge packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null &&
                    player.containerMenu instanceof SmithingScreenHandlerReforge handler &&
                    player.containerMenu.containerId == packet.containerId) {
                handler.modifiers$tryReforge();
            }
        });
        ctx.get().setPacketHandled(true);
    }
}


