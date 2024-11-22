package kr.kro.teamdodoco.extra_elytra.server;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class ExtraElytraServer implements ModInitializer {
    private static final Identifier MOD_CHECK_CHANNEL = new Identifier("extra_elytra", "mod_check");

    private final Map<ServerPlayerEntity, Integer> pendingPlayers = new HashMap<>();

    @Override
    public void onInitialize() {
        System.out.println("[Extra Elytra] Extra Elytra Server Event Registered");

        // 플레이어가 서버에 접속할 때 이벤트 등록
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
        {
            //모드 채널로 패킷 보내기
            ServerPlayNetworking.send(handler.player, MOD_CHECK_CHANNEL, PacketByteBufs.empty());
        });
    }
}
