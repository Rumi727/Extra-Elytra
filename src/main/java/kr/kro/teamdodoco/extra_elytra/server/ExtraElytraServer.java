package kr.kro.teamdodoco.extra_elytra.server;

import kr.kro.teamdodoco.extra_elytra.ModCheckPayload;
import kr.kro.teamdodoco.extra_elytra.UpdateMotionPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

public class ExtraElytraServer implements ModInitializer
{
    @Override
    public void onInitialize() {
        System.out.println("[Extra Elytra] Extra Elytra Server Event Registered");

        PayloadTypeRegistry.playS2C().register(ModCheckPayload.ID, ModCheckPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(UpdateMotionPayload.ID, UpdateMotionPayload.CODEC);

        // 플레이어가 서버에 접속할 때 이벤트 등록
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
        {
            //모드 채널로 패킷 보내기
            ServerPlayNetworking.send(handler.player, new ModCheckPayload());
        });

        ServerPlayNetworking.registerGlobalReceiver(UpdateMotionPayload.ID, (payload, context) ->
        {
            // 클라이언트에서 온 업데이트 모션 채널 패킷 받기
            ServerPlayerEntity player = context.player();
            if (player.isFallFlying())
                player.setVelocity(payload.motion());
        });
    }
}

