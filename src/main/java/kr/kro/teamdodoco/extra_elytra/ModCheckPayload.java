package kr.kro.teamdodoco.extra_elytra;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record ModCheckPayload() implements CustomPacketPayload
{
    private static final Identifier MOD_CHECK_CHANNEL = Identifier.fromNamespaceAndPath("extra_elytra", "mod_check");

    public static final Type<ModCheckPayload> ID = new Type<>(MOD_CHECK_CHANNEL);
    public static final StreamCodec<RegistryFriendlyByteBuf, ModCheckPayload> CODEC = new StreamCodec<>() {
        @Override
        public ModCheckPayload decode(RegistryFriendlyByteBuf buf) { return new ModCheckPayload(); }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, ModCheckPayload value) {}
    };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
