package kr.kro.teamdodoco.extra_elytra;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ModCheckPayload() implements CustomPayload
{
    private static final Identifier MOD_CHECK_CHANNEL = Identifier.of("extra_elytra", "mod_check");

    public static final Id<ModCheckPayload> ID = new Id<>(MOD_CHECK_CHANNEL);
    public static final PacketCodec<RegistryByteBuf, ModCheckPayload> CODEC = new PacketCodec<RegistryByteBuf, ModCheckPayload>() {
        @Override
        public ModCheckPayload decode(RegistryByteBuf buf) { return new ModCheckPayload(); }

        @Override
        public void encode(RegistryByteBuf buf, ModCheckPayload value) {}
    };

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
