package com.github.vfyjxf.ae2utilities.network;

import com.github.vfyjxf.ae2utilities.AE2Utilities;
import com.github.vfyjxf.ae2utilities.network.packets.PacketSwitchGuis;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class Ae2uNetworkHandler {

    private static final SimpleNetworkWrapper INSTANCE = new SimpleNetworkWrapper(AE2Utilities.MODID);
    private static int packId = 0;

    private static int nextId() {
        return packId++;
    }

    public static void init() {
        INSTANCE.registerMessage(PacketSwitchGuis.Handler.class, PacketSwitchGuis.class, nextId(), Side.SERVER);
    }

    public static void sendToServer(IMessage message) {
        INSTANCE.sendToServer(message);
    }

    public static void sendToPlayer(IMessage message, EntityPlayerMP player) {
        INSTANCE.sendTo(message, player);
    }

    public static void sendToAll(IMessage message) {
        INSTANCE.sendToAll(message);

    }

    public static void sent(IMessage message, int dimensionId) {
        INSTANCE.sendToDimension(message, dimensionId);
    }

}
