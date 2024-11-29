package kr.kro.teamdodoco.extra_elytra.server;

import kr.kro.teamdodoco.extra_elytra.client.ExtraElytraConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class ExtraElytraServer implements ModInitializer {
    private static final Identifier MOD_CHECK_CHANNEL = new Identifier("extra_elytra", "mod_check");
    private static final Identifier UPDATE_MOTION_CHANNEL = new Identifier("extra_elytra", "update_motion");

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

        ServerPlayNetworking.registerGlobalReceiver(UPDATE_MOTION_CHANNEL, (server, player, handler, buf, responseSender) ->
        {
            if (player.isFallFlying())
                player.setVelocity(buf.readDouble(), buf.readDouble(), buf.readDouble());
        });
    }
}
