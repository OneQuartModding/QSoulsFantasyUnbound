package net.onequart.qsouls.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.onequart.qsouls.QSouls;

public class ModMessages {
    private static SimpleChannel INSTANCE;
    private static int packetId = 0;
    private static int id() { return packetId++; }
    public static <MSG> void send(PacketDistributor.PacketTarget target, MSG message) { INSTANCE.send(target, message); }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(QSouls.MOD_ID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(StartDashC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(StartDashC2SPacket::new)
                .encoder(StartDashC2SPacket::toBytes)
                .consumerMainThread(StartDashC2SPacket::handle)
                .add();

        net.messageBuilder(DamageNumberS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(DamageNumberS2CPacket::new)
                .encoder(DamageNumberS2CPacket::toBytes)
                .consumerMainThread(DamageNumberS2CPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }
}