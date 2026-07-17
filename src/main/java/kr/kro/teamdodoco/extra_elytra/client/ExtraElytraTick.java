package kr.kro.teamdodoco.extra_elytra.client;

import kr.kro.teamdodoco.extra_elytra.UpdateMotionPayload;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.LivingEntity;

public class ExtraElytraTick
{
    static int jumpTimer;

    public static void onInitialize()
    {
        ClientTickEvents.START_CLIENT_TICK.register(client ->
        {
            onUpdate(client);
        });
    }

    static void onUpdate(Minecraft client)
    {
        if (!ExtraElytraConfig.config.enableMod || !ExtraElytraClient.GetIsServerModInstalled())
            return;

        if (client.player == null || client.level == null)
            return;

        if(jumpTimer > 0)
            jumpTimer--;

        if(client.player.isFallFlying())
        {
            if(ExtraElytraConfig.config.stopInWater && client.player.isInWater())
            {
                sendStartStopPacket(client);
                return;
            }

            controlSpeed(client);
            controlHeight(client);

            //서버로 업데이트 모션 채널 패킷 보내기
            ClientPlayNetworking.send(new UpdateMotionPayload(client.player.getDeltaMovement()));
            return;
        }

        ItemStack chest = client.player.getItemBySlot(EquipmentSlot.CHEST);
        if(chest.getItem() != Items.ELYTRA)
            return;

        if(LivingEntity.canGlideUsing(chest, EquipmentSlot.CHEST) && client.options.keyJump.isDown())
            doInstantFly(client);
    }

    static void sendStartStopPacket(Minecraft client)
    {
        ServerboundPlayerCommandPacket packet = new ServerboundPlayerCommandPacket(client.player,
                ServerboundPlayerCommandPacket.Action.START_FALL_FLYING);
        client.player.connection.send(packet);
    }

    static double currentHoverYSpeed = 0;
    static void controlHeight(Minecraft client)
    {
        if(!ExtraElytraConfig.config.heightCtrl)
            return;

        Vec3 v = client.player.getDeltaMovement();

        if (ExtraElytraConfig.config.hovering && client.options.keyJump.isDown() && client.options.keyShift.isDown())
        {
            float pitch = (float)Math.toRadians(client.player.getXRot());
            float pitchCos = Mth.cos(pitch);
            double sqrPitchCos = pitchCos * pitchCos;

            double hoverYSpeed = -0.08 + sqrPitchCos * 0.06;
            currentHoverYSpeed = Mth.lerp(0.2, currentHoverYSpeed, 0);

            v = new Vec3(v.x, currentHoverYSpeed - hoverYSpeed, v.z);
        }
        else
        {
            currentHoverYSpeed = v.y;
            if (client.options.keyJump.isDown())
                v = v.add(0, 0.08, 0);
            else if (client.options.keyShift.isDown())
                v = v.subtract(0, 0.04, 0);
        }

        client.player.setDeltaMovement(v);
    }

    static void controlSpeed(Minecraft client)
    {
        if(!ExtraElytraConfig.config.speedCtrl)
            return;

        float yaw = (float)Math.toRadians(client.player.getYRot());
        Vec3 forward = new Vec3(-Mth.sin(yaw) * 0.05, 0,
                Mth.cos(yaw) * 0.05);

        Vec3 v = client.player.getDeltaMovement();
        if (client.options.keyUp.isDown())
            v = v.add(forward);
        if (client.options.keyDown.isDown())
            v = v.subtract(forward);
        if (client.options.keyLeft.isDown())
            v = v.add(forward.yRot(90).scale(1.7));
        if (client.options.keyRight.isDown())
            v = v.add(forward.yRot(-90).scale(1.7));

        client.player.setDeltaMovement(v);
    }

    static void doInstantFly(Minecraft client)
    {
        if(!ExtraElytraConfig.config.instantFly)
            return;

        if(jumpTimer <= 0)
        {
            jumpTimer = 20;
            client.player.setJumping(false);
            client.player.setSprinting(true);
            client.player.jumpFromGround();
        }

        sendStartStopPacket(client);
    }
}
