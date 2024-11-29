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

        ItemStack chest = MinecraftClient.getInstance().player.getEquippedStack(EquipmentSlot.CHEST);
        if(chest.getItem() != Items.ELYTRA)
            return;

        if(MinecraftClient.getInstance().player.isFallFlying())
        {
            if(ExtraElytraConfig.config.stopInWater && MinecraftClient.getInstance().player.isTouchingWater())
            {
                sendStartStopPacket();
                return;
            }

            controlSpeed();
            controlHeight();

            PacketByteBuf buf = PacketByteBufs.create();
            Vec3d motion = client.player.getVelocity();

            buf.writeDouble(motion.x);
            buf.writeDouble(motion.y);
            buf.writeDouble(motion.z);

            ClientPlayNetworking.send(UPDATE_MOTION_CHANNEL, buf);
            return;
        }

        if(ElytraItem.isUsable(chest) && MinecraftClient.getInstance().options.jumpKey.isPressed())
            doInstantFly();
    }

    static void sendStartStopPacket()
    {
        ClientCommandC2SPacket packet = new ClientCommandC2SPacket(MinecraftClient.getInstance().player,
                ClientCommandC2SPacket.Mode.START_FALL_FLYING);
        MinecraftClient.getInstance().player.networkHandler.sendPacket(packet);
    }

    static void controlHeight()
    {
        if(!ExtraElytraConfig.config.heightCtrl)
            return;

        Vec3d v = MinecraftClient.getInstance().player.getVelocity();

        if(MinecraftClient.getInstance().options.jumpKey.isPressed())
            MinecraftClient.getInstance().player.setVelocity(v.x, v.y + 0.08, v.z);
        else if(MinecraftClient.getInstance().options.sneakKey.isPressed())
            MinecraftClient.getInstance().player.setVelocity(v.x, v.y - 0.04, v.z);
    }

    static void controlSpeed()
    {
        if(!ExtraElytraConfig.config.speedCtrl)
            return;

        float yaw = (float)Math.toRadians(MinecraftClient.getInstance().player.getYaw());
        Vec3d forward = new Vec3d(-MathHelper.sin(yaw) * 0.05, 0,
                MathHelper.cos(yaw) * 0.05);

        Vec3d v = MinecraftClient.getInstance().player.getVelocity();

        if(MinecraftClient.getInstance().options.forwardKey.isPressed())
            MinecraftClient.getInstance().player.setVelocity(v.add(forward));
        else if(MinecraftClient.getInstance().options.backKey.isPressed())
            MinecraftClient.getInstance().player.setVelocity(v.subtract(forward));
    }

    static void doInstantFly()
    {
        if(!ExtraElytraConfig.config.instantFly)
            return;

        if(jumpTimer <= 0)
        {
            jumpTimer = 20;
            MinecraftClient.getInstance().player.setJumping(false);
            MinecraftClient.getInstance().player.setSprinting(true);
            MinecraftClient.getInstance().player.jump();
        }

        sendStartStopPacket();
    }
}
