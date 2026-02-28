package net.onequart.qsouls.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.onequart.qsouls.common.skill.HeavyMeleeManager;

import java.util.function.Supplier;

public class StartDashC2SPacket {

    public StartDashC2SPacket() {}
    public StartDashC2SPacket(FriendlyByteBuf buf) {}
    public void toBytes(FriendlyByteBuf buf) {}

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                HeavyMeleeManager.startDash(player);
            }
        });
        return true;
    }
}