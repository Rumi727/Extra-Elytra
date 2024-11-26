package kr.kro.teamdodoco.extra_elytra.client;

import kr.kro.teamdodoco.extra_elytra.ModCheckPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ExtraElytraClient implements ClientModInitializer {

    static boolean isServerModInstalled = false;
    static final Identifier MOD_CHECK_CHANNEL = Identifier.of("extra_elytra", "mod_check");

    @Override
    public void onInitializeClient()
    {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) ->
        {
            DisableMod();
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) ->
        {
            DisableMod();
        });

        ClientPlayNetworking.registerGlobalReceiver(ModCheckPayload.ID, (payload, context) ->
        {
            // 서버에서 온 모드 채널 패킷 받기
            context.client().execute(() ->
            {
                System.out.println("[Extra Elytra] Mod is installed on the server!");
                EnableMod();

                if (ExtraElytraConfig.config.chatLog)
                    MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.literal("§b[Extra Elytra]§r Mod is installed on the server!"));
            });
        });

        ExtraElytraTick.onInitialize();

        System.out.println("[Extra Elytra] Extra Elytra Client Event Registered");
    }

    public static boolean GetIsServerModInstalled()
    {
        return isServerModInstalled;
    }

    static void EnableMod()
    {
        isServerModInstalled = true;
        System.out.println("[Extra Elytra] Enabled Extra Elytra");
    }

    static void DisableMod()
    {
        isServerModInstalled = false;
        System.out.println("[Extra Elytra] Disabled Extra Elytra");
    }
}
