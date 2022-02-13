package com.github.vfyjxf.ae2utilities.network;

import appeng.api.parts.IPart;
import appeng.api.parts.IPartHost;
import appeng.api.util.AEPartLocation;
import appeng.container.AEBaseContainer;
import appeng.container.ContainerOpenContext;
import appeng.container.implementations.ContainerPriority;
import appeng.helpers.IInterfaceHost;
import com.github.vfyjxf.ae2utilities.AE2Utilities;
import com.github.vfyjxf.ae2utilities.client.gui.GuiAe2uPriority;
import com.github.vfyjxf.ae2utilities.client.gui.GuiEnhancedInterface;
import com.github.vfyjxf.ae2utilities.container.ContainerEnhancedInterface;
import com.github.vfyjxf.ae2utilities.parts.PartEnhancedInterfaceBase;
import com.github.vfyjxf.ae2utilities.tile.TileEnhancedInterfaceBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class Ae2uGuiHandler implements IGuiHandler {

    public static final int GUI_ENHANCED_INTERFACE_TIER_1_ID = 1;
    public static final int GUI_ENHANCED_INTERFACE_TIER_2_ID = 2;
    public static final int GUI_ENHANCED_INTERFACE_TIER_3_ID = 3;
    public static final int GUI_PRIORITY_ID = 4;

    @Nullable
    @Override
    public Object getServerGuiElement(int ordinal, EntityPlayer player, World world, int x, int y, int z) {
        final int guiId = ordinal >> 4;
        final AEPartLocation side = AEPartLocation.fromOrdinal(ordinal & 7);
        if (side != AEPartLocation.INTERNAL) {
            TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
            IPart part = getPart(tile, side);
            if (tile != null) {
                switch (guiId) {
                    case GUI_ENHANCED_INTERFACE_TIER_1_ID:
                    case GUI_ENHANCED_INTERFACE_TIER_2_ID:
                    {
                        int tier = part != null ? getTier(part) : getTier(tile);
                        if (tier >= 0) {
                            IInterfaceHost host = part != null ? (IInterfaceHost) part : (IInterfaceHost) tile;
                            return updateGui(new ContainerEnhancedInterface(player.inventory, host, tier), world, x, y, z, side, host);
                        }
                    }
                    break;
                    case GUI_ENHANCED_INTERFACE_TIER_3_ID:
                    {
                        int tier = part != null ? getTier(part) : getTier(tile);
                        if (tier >= 0) {
                            IInterfaceHost host = part != null ? (IInterfaceHost) part : (IInterfaceHost) tile;
                            ContainerEnhancedInterface cei = new ContainerEnhancedInterface(player.inventory, host, tier) {
                                @Override
                                protected int getHeight() {
                                    return 217;
                                }
                            };
                            return updateGui(cei, world, x, y, z, side, host);
                        }
                    }
                    break;
                    case GUI_PRIORITY_ID:
                        if (tile instanceof TileEnhancedInterfaceBase) {
                            return updateGui(new ContainerPriority(player.inventory, (TileEnhancedInterfaceBase) tile), world, x, y, z, side, tile);
                        } else if (part instanceof PartEnhancedInterfaceBase) {
                            return updateGui(new ContainerPriority(player.inventory, (PartEnhancedInterfaceBase) part), world, x, y, z, side, part);
                        }
                    default:
                        break;
                }
            }
        }
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ordinal, EntityPlayer player, World world, int x, int y, int z) {
        final int guiId = ordinal >> 4;
        final AEPartLocation side = AEPartLocation.fromOrdinal(ordinal & 7);
        if (side != AEPartLocation.INTERNAL) {
            TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
            IPart part = getPart(tile, side);
            if (tile != null) {
                switch (guiId) {
                    case GUI_ENHANCED_INTERFACE_TIER_1_ID:
                    case GUI_ENHANCED_INTERFACE_TIER_2_ID:
                    case GUI_ENHANCED_INTERFACE_TIER_3_ID:
                    {
                        int tier = part != null ? getTier(part) : getTier(tile);
                        if (tier >= 0) {
                            IInterfaceHost host = part != null ? (IInterfaceHost) part : (IInterfaceHost) tile;
                            return new GuiEnhancedInterface(player.inventory, host, tier);
                        }
                    }
                    break;
                    case GUI_PRIORITY_ID:
                        if (tile instanceof TileEnhancedInterfaceBase) {
                            return new GuiAe2uPriority(player.inventory, (TileEnhancedInterfaceBase) tile);
                        } else if (part instanceof PartEnhancedInterfaceBase) {
                            return new GuiAe2uPriority(player.inventory, (PartEnhancedInterfaceBase) part);
                        }
                    default:
                        break;
                }
            }
        }
        return null;
    }

    private Object updateGui(Object newContainer, final World w, final int x, final int y, final int z, final AEPartLocation side, final Object myItem) {
        if (newContainer instanceof AEBaseContainer) {
            final AEBaseContainer bc = (AEBaseContainer) newContainer;
            bc.setOpenContext(new ContainerOpenContext(myItem));
            bc.getOpenContext().setWorld(w);
            bc.getOpenContext().setX(x);
            bc.getOpenContext().setY(y);
            bc.getOpenContext().setZ(z);
            bc.getOpenContext().setSide(side);
        }
        return newContainer;
    }

    private IPart getPart(TileEntity tile, AEPartLocation side) {
        if (tile instanceof IPartHost) {
            IPartHost host = (IPartHost) tile;
            return host.getPart(side);
        }
        return null;
    }

    private int getTier(Object tile) {
        if (tile instanceof TileEnhancedInterfaceBase) {
            return ((TileEnhancedInterfaceBase) tile).getTier();
        }
        if (tile instanceof PartEnhancedInterfaceBase) {
            return ((PartEnhancedInterfaceBase) tile).getTier();
        }
        return -1;
    }

    public static void openGui(EntityPlayer player, int ID, TileEntity tile, AEPartLocation side) {
        int x = (int) player.posX;
        int y = (int) player.posY;
        int z = (int) player.posZ;
        if (tile != null) {
            x = tile.getPos().getX();
            y = tile.getPos().getY();
            z = tile.getPos().getZ();
        }
        if (tile != null) {
            player.openGui(AE2Utilities.instance, (ID << 4 | side.ordinal()), tile.getWorld(), x, y, z);
        } else {
            player.openGui(AE2Utilities.instance, (ID << 4 | side.ordinal()), player.getEntityWorld(), x, y, z);
        }
    }


}
