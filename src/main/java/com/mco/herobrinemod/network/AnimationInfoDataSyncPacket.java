package com.mco.herobrinemod.network;

import com.mco.herobrinemod.capabilities.AnimationInfoProvider;
import com.mco.herobrinemod.client.ClientAnimationInfoData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

public class AnimationInfoDataSyncPacket {
    private final int animationTicks;
    private final int animationNameLength;
    private final String animation;

    public AnimationInfoDataSyncPacket(int animationTicks, String animation) {
        this.animationTicks = animationTicks;
        this.animationNameLength = animation == null ? 0 : animation.length();
        this.animation = animation;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.animationTicks);
        buf.writeInt(this.animationNameLength);
        byte[] bytes = animation == null ? "null".getBytes() : animation.getBytes();
        buf.writeBytes(bytes);
    }

    public AnimationInfoDataSyncPacket(FriendlyByteBuf buf) {
        this.animationTicks = buf.readInt();
        this.animationNameLength = buf.readInt();
        this.animation = buf.readBytes(animationNameLength).toString(StandardCharsets.UTF_8);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientAnimationInfoData.setAnimationTicks(this.animationTicks);
            ClientAnimationInfoData.setAnimation(this.animation);
        });
        supplier.get().setPacketHandled(true);
        return true;
    }
}
