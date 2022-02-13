package com.github.vfyjxf.ae2utilities.network.packets;

import appeng.api.util.AEPartLocation;
import appeng.container.AEBaseContainer;
import appeng.container.ContainerOpenContext;
import com.github.vfyjxf.ae2utilities.network.Ae2uGuiHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;


public class PacketSwitchGuis implements IMessage {

    private int guiId;
    private boolean isAeGui;

    public PacketSwitchGuis() {

    }

    public PacketSwitchGuis(int guiId) {
        this.guiId = guiId;
        this.isAeGui = false;
    }

    public PacketSwitchGuis(int guiId, boolean isAeGui) {
        this.guiId = guiId;
        this.isAeGui = isAeGui;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.guiId = buf.readInt();
        this.isAeGui = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.guiId);
        buf.writeBoolean(this.isAeGui);
    }

    public static class Handler implements IMessageHandler<PacketSwitchGuis, IMessage> {
        @Override
        public IMessage onMessage(PacketSwitchGuis message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            Container container = player.openContainer;
            if (message.isAeGui) {
                if (container instanceof AEBaseContainer) {
                    ContainerOpenContext context = ((AEBaseContainer) container).getOpenContext();
                    if (context != null) {
                        TileEntity te = context.getTile();
                        Ae2uGuiHandler.openGui(player, message.guiId, te, (message.isAeGui ? context.getSide() : AEPartLocation.INTERNAL));
                    }
                }
            }
            return null;
        }
    }

}
