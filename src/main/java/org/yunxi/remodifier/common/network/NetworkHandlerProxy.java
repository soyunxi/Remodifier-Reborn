package org.yunxi.remodifier.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface NetworkHandlerProxy {
    <MSG> void registerMessage(Class<MSG> messageType,
                               BiConsumer<MSG, FriendlyByteBuf> encoder,
                               Function<FriendlyByteBuf, MSG> decoder,
                               BiConsumer<MSG, Supplier<NetworkEvent.Context>> handler,
                               NetworkDirection direction);

    <MSG> void sendToServer(MSG message);
    <MSG> void sendTo(MSG message, net.minecraft.server.level.ServerPlayer player);
    <MSG> void sendToAllPlayers(MSG message);
}
