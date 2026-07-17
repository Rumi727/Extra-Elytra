package kr.kro.teamdodoco.extra_elytra;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;

public record UpdateMotionPayload(Vec3 motion) implements CustomPacketPayload
{
    static final Identifier MOD_CHECK_CHANNEL = Identifier.fromNamespaceAndPath("extra_elytra", "motion_update");

    public static final Type<UpdateMotionPayload> ID = new Type<>(MOD_CHECK_CHANNEL);
    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateMotionPayload> CODEC = new StreamCodec<>()
    {
        @Override
        public UpdateMotionPayload decode(RegistryFriendlyByteBuf buf)
        {
            Vec3 motion = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
            return new UpdateMotionPayload(motion);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, UpdateMotionPayload value)
        {
            buf.writeDouble(value.motion.x);
            buf.writeDouble(value.motion.y);
            buf.writeDouble(value.motion.z);
        }
    };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
