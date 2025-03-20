package org.yunxi.remodifier.common.network;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class NetworkHandlerForge implements NetworkHandlerProxy {
    private static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            ResourceLocation.tryBuild("remodifier", "net"),
            () -> "1",
            s -> true,
            s -> true
    );
    private static int messageID = 0;

    @Override
    public <MSG> void registerMessage(Class<MSG> clazz,
                                      BiConsumer<MSG, FriendlyByteBuf> encode,
                                      Function<FriendlyByteBuf, MSG> decode,
                                      BiConsumer<MSG, Supplier<NetworkEvent.Context>> handler,
                                      NetworkDirection direction) {
        Optional<NetworkDirection> networkDirection = Optional.ofNullable(direction);

        CHANNEL.registerMessage(
                messageID++,
                clazz,
                encode,
                decode,
                (msg, contextSupplier) -> {
                    NetworkEvent.Context ctx = contextSupplier.get();
                    handler.accept(msg, () -> ctx);
                },
                networkDirection // 传递动态生成的方向
        );
    }

    @Override
    public <MSG> void sendToServer(MSG packet) {
        CHANNEL.sendToServer(packet);
    }

    @Override
    public <MSG> void sendTo(MSG packet, net.minecraft.server.level.ServerPlayer player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }

    @Override
    public <MSG> void sendToAllPlayers(MSG packet) {
        CHANNEL.send(PacketDistributor.ALL.noArg(), packet);
    }
}
