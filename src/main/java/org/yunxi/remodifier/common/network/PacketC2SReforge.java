package org.yunxi.remodifier.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketC2SReforge {
    public PacketC2SReforge() {}  // 空构造器必须保留

    public PacketC2SReforge(FriendlyByteBuf buf) {}  // Forge 使用 FriendlyByteBuf [<sup data-citation='{&quot;url&quot;:&quot;https://forums.minecraftforge.net/topic/139825-forge-473-minecraft-1201/&quot;,&quot;title&quot;:&quot;Forge 47.3 Minecraft 1.20.1 - Releases - Forge Forums&quot;,&quot;content&quot;:&quot;Forge version: 47.3.0 Minecraft version: 1.20.1 Downloads: Changelog: (Direct) Installer: (AdFocus) (Direct) MDK: (AdFocus) (Direct) Downloads page Intro: This third recommended build for MC 1.20.1 offers improved performance, bugfixes, new features and various backports from newer versions such&quot;}'>4</sup>](https://forums.minecraftforge.net/topic/139825-forge-473-minecraft-1201/)

    public void encode(FriendlyByteBuf buf) {}      // 实现编解码方法

    public static void handle(PacketC2SReforge packet, Supplier<NetworkEvent.Context> ctx) {
        // 处理逻辑已迁移到 NetworkHandler
    }
}

