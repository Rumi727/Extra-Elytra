package kr.kro.teamdodoco.extra_elytra.server;

import kr.kro.teamdodoco.extra_elytra.ModCheckPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class ExtraElytraServer implements ModInitializer
{
    @Override
    public void onInitialize() {
        System.out.println("[Extra Elytra] Extra Elytra Server Event Registered");

        PayloadTypeRegistry.playS2C().register(ModCheckPayload.ID, ModCheckPayload.CODEC);

        // 플레이어가 서버에 접속할 때 이벤트 등록
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
        {
            //모드 채널로 패킷 보내기
            ServerPlayNetworking.send(handler.player, new ModCheckPayload());
        });
    }
}

