package net.onequart.qsouls.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.onequart.qsouls.client.ClientPacketHandler;

import java.util.function.Supplier;

public class DamageNumberS2CPacket {
    private final int entityId;
    private final float damage;

    public DamageNumberS2CPacket(int entityId, float damage) {
        this.entityId = entityId;
        this.damage = damage;
    }

    public DamageNumberS2CPacket(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.damage = buf.readFloat();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeFloat(damage);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientPacketHandler.handleDamageNumber(entityId, damage);
        });
        return true;
    }
}