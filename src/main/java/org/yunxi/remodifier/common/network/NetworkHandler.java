package org.yunxi.remodifier.common.network;

import net.minecraftforge.network.PacketDistributor;
import org.yunxi.remodifier.Remodifier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.thread.BlockableEventLoop;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.yunxi.remodifier.common.reforge.SmithingScreenHandlerReforge;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            ResourceLocation.tryBuild(Remodifier.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        int packetId = 0;
        CHANNEL.registerMessage(packetId++,
                PacketC2SReforge.class,
                PacketC2SReforge::encode,
                PacketC2SReforge::new,
                NetworkHandler::handleReforgePacket,
                Optional.of(NetworkDirection.PLAY_TO_SERVER)
        );
    }

    private static void handleReforgePacket(PacketC2SReforge packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null && player.containerMenu instanceof SmithingScreenHandlerReforge handler) {
                handler.modifiers$tryReforge();
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public static void sendToServer(Object packet) {
        CHANNEL.sendToServer(packet);
    }

    public static void sendTo(Object packet, ServerPlayer player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }
}

