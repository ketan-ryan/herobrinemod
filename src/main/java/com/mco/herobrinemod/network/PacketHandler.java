package com.mco.herobrinemod.network;

import com.mco.herobrinemod.HerobrineMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {
    private PacketHandler() {}

    private static final String PROTOCOL_VERSION = "1";

    private static SimpleChannel INSTANCE;

    public static void init() {
        INSTANCE = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(HerobrineMod.MODID, "main"),
                () -> PROTOCOL_VERSION,
                PROTOCOL_VERSION::equals,
                PROTOCOL_VERSION::equals);

        int index = 0;

        // Client -> Server
        INSTANCE.registerMessage(index++, AnimationInfoDataSyncPacket.class, AnimationInfoDataSyncPacket::toBytes,
                AnimationInfoDataSyncPacket::new, AnimationInfoDataSyncPacket::handle);

    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToClient(MSG message, ServerPlayer player) {
        INSTANCE.sendTo(message, player.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
    }
}
