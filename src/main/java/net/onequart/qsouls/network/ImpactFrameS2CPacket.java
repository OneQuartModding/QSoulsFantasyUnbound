package net.onequart.qsouls.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.onequart.qsouls.client.render.ImpactFrameManager;

import java.util.function.Supplier;

public class ImpactFrameS2CPacket {
    private final int ticks;
    private final int color;

    public ImpactFrameS2CPacket(int ticks, int color) {
        this.ticks = ticks;
        this.color = color;
    }

    public ImpactFrameS2CPacket(FriendlyByteBuf buffer) {
        this.ticks = buffer.readInt();
        this.color = buffer.readInt();
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeInt(this.ticks);
        buffer.writeInt(this.color);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ImpactFrameManager.trigger(this.ticks, this.color);
        });
        context.setPacketHandled(true);
        return true;
    }
}