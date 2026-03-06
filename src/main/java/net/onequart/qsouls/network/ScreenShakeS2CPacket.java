package net.onequart.qsouls.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.onequart.qsouls.client.render.CameraShakeManager;

import java.util.function.Supplier;

public class ScreenShakeS2CPacket {
    private final int ticks;
    private final float intensity;

    public ScreenShakeS2CPacket(int ticks, float intensity) {
        this.ticks = ticks;
        this.intensity = intensity;
    }

    public ScreenShakeS2CPacket(FriendlyByteBuf buffer) {
        this.ticks = buffer.readInt();
        this.intensity = buffer.readFloat();
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeInt(this.ticks);
        buffer.writeFloat(this.intensity);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            CameraShakeManager.trigger(this.ticks, this.intensity);
        });
        context.setPacketHandled(true);
        return true;
    }
}