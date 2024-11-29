package kr.kro.teamdodoco.extra_elytra.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class ExtraElytraTick
{
    private static final Identifier UPDATE_MOTION_CHANNEL = new Identifier("extra_elytra", "update_motion");

    static int jumpTimer;

    public static void onInitialize()
    {
        ClientTickEvents.START_CLIENT_TICK.register(client ->
        {
            onUpdate(client);
        });
    }

    static void onUpdate(MinecraftClient client)
    {
        if (!ExtraElytraConfig.config.enableMod || !ExtraElytraClient.GetIsServerModInstalled())
            return;

        if (client.player == null || client.world == null)
            return;

        if(jumpTimer > 0)
            jumpTimer--;

        ItemStack chest = client.player.getEquippedStack(EquipmentSlot.CHEST);
        if(chest.getItem() != Items.ELYTRA)
            return;

        if(client.player.isFallFlying())
        {
            if(ExtraElytraConfig.config.stopInWater && client.player.isTouchingWater())
            {
                sendStartStopPacket(client);
                return;
            }

            controlSpeed(client);
            controlHeight(client);

            PacketByteBuf buf = PacketByteBufs.create();
            Vec3d motion = client.player.getVelocity();

            buf.writeDouble(motion.x);
            buf.writeDouble(motion.y);
            buf.writeDouble(motion.z);

            ClientPlayNetworking.send(UPDATE_MOTION_CHANNEL, buf);
            return;
        }

        if(ElytraItem.isUsable(chest) && client.options.jumpKey.isPressed())
            doInstantFly(client);
    }

    static void sendStartStopPacket(MinecraftClient client)
    {
        ClientCommandC2SPacket packet = new ClientCommandC2SPacket(client.player,
                ClientCommandC2SPacket.Mode.START_FALL_FLYING);
        client.player.networkHandler.sendPacket(packet);
    }

    static void controlHeight(MinecraftClient client)
    {
        if(!ExtraElytraConfig.config.heightCtrl)
            return;

        Vec3d v = client.player.getVelocity();

        if(client.options.jumpKey.isPressed())
            client.player.setVelocity(v.x, v.y + 0.08, v.z);
        else if(client.options.sneakKey.isPressed())
            client.player.setVelocity(v.x, v.y - 0.04, v.z);
    }

    static void controlSpeed(MinecraftClient client)
    {
        if(!ExtraElytraConfig.config.speedCtrl)
            return;

        float yaw = (float)Math.toRadians(client.player.getYaw());
        Vec3d forward = new Vec3d(-MathHelper.sin(yaw) * 0.05, 0,
                MathHelper.cos(yaw) * 0.05);

        Vec3d v = client.player.getVelocity();
        if (client.options.forwardKey.isPressed())
            v = v.add(forward);
        if (client.options.backKey.isPressed())
            v = v.subtract(forward);
        if (client.options.leftKey.isPressed())
            v = v.add(forward.rotateY(90).multiply(2));
        if (client.options.rightKey.isPressed())
            v = v.add(forward.rotateY(-90).multiply(2));

        client.player.setVelocity(v);
    }

    static void doInstantFly(MinecraftClient client)
    {
        if(!ExtraElytraConfig.config.instantFly)
            return;

        if(jumpTimer <= 0)
        {
            jumpTimer = 20;
            client.player.setJumping(false);
            client.player.setSprinting(true);
            client.player.jump();
        }

        sendStartStopPacket(client);
    }
}
