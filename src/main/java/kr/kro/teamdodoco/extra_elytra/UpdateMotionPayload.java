package kr.kro.teamdodoco.extra_elytra;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public record UpdateMotionPayload(Vec3d motion) implements CustomPayload
{
    static final Identifier MOD_CHECK_CHANNEL = Identifier.of("extra_elytra", "motion_update");

    public static final Id<UpdateMotionPayload> ID = new Id<>(MOD_CHECK_CHANNEL);
    public static final PacketCodec<RegistryByteBuf, UpdateMotionPayload> CODEC = new PacketCodec<>()
    {
        @Override
        public UpdateMotionPayload decode(RegistryByteBuf buf)
        {
            Vec3d motion = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
            return new UpdateMotionPayload(motion);
        }

        @Override
        public void encode(RegistryByteBuf buf, UpdateMotionPayload value)
        {
            buf.writeDouble(value.motion.x);
            buf.writeDouble(value.motion.y);
            buf.writeDouble(value.motion.z);
        }
    };

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
