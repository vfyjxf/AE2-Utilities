package com.github.vfyjxf.ae2utilities.mixins;

import appeng.api.config.Settings;
import appeng.api.config.YesNo;
import appeng.api.networking.IGrid;
import appeng.api.networking.IGridNode;
import appeng.api.networking.security.IActionHost;
import appeng.container.AEBaseContainer;
import appeng.container.implementations.ContainerInterfaceTerminal;
import appeng.core.sync.network.NetworkHandler;
import appeng.core.sync.packets.PacketCompressedNBT;
import appeng.helpers.DualityInterface;
import appeng.helpers.IInterfaceHost;
import appeng.helpers.InventoryAction;
import appeng.items.misc.ItemEncodedPattern;
import appeng.parts.misc.PartInterface;
import appeng.tile.misc.TileInterface;
import appeng.util.InventoryAdaptor;
import appeng.util.Platform;
import appeng.util.helpers.ItemHandlerUtil;
import appeng.util.inv.AdaptorItemHandler;
import appeng.util.inv.WrapperCursorItemHandler;
import appeng.util.inv.WrapperFilteredItemHandler;
import appeng.util.inv.WrapperRangeItemHandler;
import appeng.util.inv.filter.IAEItemFilter;
import com.github.vfyjxf.ae2utilities.parts.PartEnhancedInterfaceTier1;
import com.github.vfyjxf.ae2utilities.parts.PartEnhancedInterfaceTier2;
import com.github.vfyjxf.ae2utilities.parts.PartEnhancedInterfaceTier3;
import com.github.vfyjxf.ae2utilities.tile.TileEnhancedInterfaceTier1;
import com.github.vfyjxf.ae2utilities.tile.TileEnhancedInterfaceTier2;
import com.github.vfyjxf.ae2utilities.tile.TileEnhancedInterfaceTier3;
import com.github.vfyjxf.ae2utilities.utils.InvTracker;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author vfyjxf
 */
@Mixin(value = ContainerInterfaceTerminal.class, remap = false)
public abstract class MixinContainerInterfaceTerminal extends AEBaseContainer {
    @Shadow
    private IGrid grid;
    @Shadow
    private NBTTagCompound data;

    private MixinContainerInterfaceTerminal(InventoryPlayer ip, Object anchor) {
        super(ip, anchor);
    }

    @Shadow
    protected abstract boolean isDifferent(ItemStack a, ItemStack b);

    private final Map<IInterfaceHost, InvTracker> ifTrackerMap = new HashMap<>();
    private final Map<Long, InvTracker> idMap = new HashMap<>();

    /**
     * @author vfyjxf
     * @reason Compatible with Enhanced Interface
     */
    @Override
    @Overwrite
    public void detectAndSendChanges() {

        if (Platform.isClient()) {
            return;
        }

        super.detectAndSendChanges();

        if (this.grid == null) {
            return;
        }

        int total = 0;
        boolean missing = false;

        final IActionHost host = this.getActionHost();
        if (host != null) {
            final IGridNode agn = host.getActionableNode();
            if (agn.isActive()) {
                for (final IGridNode gn : this.grid.getMachines(TileInterface.class)) {
                    if (gn.isActive()) {
                        final IInterfaceHost ih = (IInterfaceHost) gn.getMachine();
                        if (ih.getInterfaceDuality().getConfigManager().getSetting(Settings.INTERFACE_TERMINAL) == YesNo.NO) {
                            continue;
                        }

                        final InvTracker t = this.ifTrackerMap.get(ih);

                        if (t == null) {
                            missing = true;
                        } else {
                            final DualityInterface dual = ih.getInterfaceDuality();
                            if (!t.getUnlocalizedName().equals(dual.getTermName())) {
                                missing = true;
                            }
                        }

                        total++;
                    }
                }

                for (final IGridNode gn : this.grid.getMachines(PartInterface.class)) {
                    if (gn.isActive()) {
                        final IInterfaceHost ih = (IInterfaceHost) gn.getMachine();
                        if (ih.getInterfaceDuality().getConfigManager().getSetting(Settings.INTERFACE_TERMINAL) == YesNo.NO) {
                            continue;
                        }

                        final InvTracker t = this.ifTrackerMap.get(ih);

                        if (t == null) {
                            missing = true;
                        } else {
                            final DualityInterface dual = ih.getInterfaceDuality();
                            if (!t.getUnlocalizedName().equals(dual.getTermName())) {
                                missing = true;
                            }
                        }

                        total++;
                    }
                }

                for (final IGridNode gn : getEnhancedInterface()) {
                    if (gn.isActive()) {
                        final IInterfaceHost ih = (IInterfaceHost) gn.getMachine();
                        if (ih.getInterfaceDuality().getConfigManager().getSetting(Settings.INTERFACE_TERMINAL) == YesNo.NO) {
                            continue;
                        }

                        final InvTracker t = this.ifTrackerMap.get(ih);

                        if (t == null) {
                            missing = true;
                        } else {
                            final DualityInterface dual = ih.getInterfaceDuality();
                            if (!t.getUnlocalizedName().equals(dual.getTermName())) {
                                missing = true;
                            }
                        }

                        total++;
                    }
                }
            }
        }

        if (total != this.ifTrackerMap.size() || missing) {
            this.regenList(this.data);
        } else {
            for (final Map.Entry<IInterfaceHost, InvTracker> en : this.ifTrackerMap.entrySet()) {
                final InvTracker inv = en.getValue();
                for (int x = 0; x < inv.getServer().getSlots(); x++) {
                    if (this.isDifferent(inv.getServer().getStackInSlot(x), inv.getClient().getStackInSlot(x))) {
                        this.addItems(this.data, inv, x, 1);
                    }
                }
            }
        }

        if (!this.data.isEmpty()) {
            try {
                NetworkHandler.instance().sendTo(new PacketCompressedNBT(this.data), (EntityPlayerMP) this.getPlayerInv().player);
            } catch (final IOException e) {
                // :P
            }
            this.data = new NBTTagCompound();
        }
    }


    /**
     * @author vfyjxf
     * @reason Compatible with Enhanced Interface
     */
    @Override
    @Overwrite
    public void doAction(EntityPlayerMP player, InventoryAction action, int slot, long id) {
        final InvTracker inv = this.idMap.get(id);
        if (inv != null) {
            final ItemStack is = inv.getServer().getStackInSlot(slot);
            final boolean hasItemInHand = !player.inventory.getItemStack().isEmpty();

            final InventoryAdaptor playerHand = new AdaptorItemHandler(new WrapperCursorItemHandler(player.inventory));

            final IItemHandler theSlot = new WrapperFilteredItemHandler(new WrapperRangeItemHandler(inv.getServer(), slot, slot + 1),
                    new IAEItemFilter() {
                        @Override
                        public boolean allowExtract(IItemHandler inv, int slot, int amount) {
                            return true;
                        }

                        @Override
                        public boolean allowInsert(IItemHandler inv, int slot, ItemStack stack) {
                            return !stack.isEmpty() && stack.getItem() instanceof ItemEncodedPattern;
                        }
                    });
            final InventoryAdaptor interfaceSlot = new AdaptorItemHandler(theSlot);

            switch (action) {
                case PICKUP_OR_SET_DOWN:

                    if (hasItemInHand) {
                        ItemStack inSlot = theSlot.getStackInSlot(0);
                        if (inSlot.isEmpty()) {
                            player.inventory.setItemStack(interfaceSlot.addItems(player.inventory.getItemStack()));
                        } else {
                            inSlot = inSlot.copy();
                            final ItemStack inHand = player.inventory.getItemStack().copy();

                            ItemHandlerUtil.setStackInSlot(theSlot, 0, ItemStack.EMPTY);
                            player.inventory.setItemStack(ItemStack.EMPTY);

                            player.inventory.setItemStack(interfaceSlot.addItems(inHand.copy()));

                            if (player.inventory.getItemStack().isEmpty()) {
                                player.inventory.setItemStack(inSlot);
                            } else {
                                player.inventory.setItemStack(inHand);
                                ItemHandlerUtil.setStackInSlot(theSlot, 0, inSlot);
                            }
                        }
                    } else {
                        ItemHandlerUtil.setStackInSlot(theSlot, 0, playerHand.addItems(theSlot.getStackInSlot(0)));
                    }

                    break;
                case SPLIT_OR_PLACE_SINGLE:

                    if (hasItemInHand) {
                        ItemStack extra = playerHand.removeItems(1, ItemStack.EMPTY, null);
                        if (!extra.isEmpty()) {
                            extra = interfaceSlot.addItems(extra);
                        }
                        if (!extra.isEmpty()) {
                            playerHand.addItems(extra);
                        }
                    } else if (!is.isEmpty()) {
                        ItemStack extra = interfaceSlot.removeItems((is.getCount() + 1) / 2, ItemStack.EMPTY, null);
                        if (!extra.isEmpty()) {
                            extra = playerHand.addItems(extra);
                        }
                        if (!extra.isEmpty()) {
                            interfaceSlot.addItems(extra);
                        }
                    }

                    break;
                case SHIFT_CLICK:

                    final InventoryAdaptor playerInv = InventoryAdaptor.getAdaptor(player);

                    ItemHandlerUtil.setStackInSlot(theSlot, 0, playerInv.addItems(theSlot.getStackInSlot(0)));

                    break;
                case MOVE_REGION:

                    final InventoryAdaptor playerInvAd = InventoryAdaptor.getAdaptor(player);
                    for (int x = 0; x < inv.getServer().getSlots(); x++) {
                        ItemHandlerUtil.setStackInSlot(inv.getServer(), x, playerInvAd.addItems(inv.getServer().getStackInSlot(x)));
                    }

                    break;
                case CREATIVE_DUPLICATE:

                    if (player.capabilities.isCreativeMode && !hasItemInHand) {
                        player.inventory.setItemStack(is.isEmpty() ? ItemStack.EMPTY : is.copy());
                    }

                    break;
                default:
                    return;
            }

            this.updateHeld(player);
        }
    }

    /**
     * @author vfyjxf
     * @reason Compatible with Enhanced Interface
     */
    @Overwrite
    private void regenList(final NBTTagCompound data) {
        this.idMap.clear();
        this.ifTrackerMap.clear();

        final IActionHost host = this.getActionHost();
        if (host != null) {
            final IGridNode agn = host.getActionableNode();
            if (agn.isActive()) {
                for (final IGridNode gn : this.grid.getMachines(TileInterface.class)) {
                    final IInterfaceHost ih = (IInterfaceHost) gn.getMachine();
                    final DualityInterface dual = ih.getInterfaceDuality();
                    if (gn.isActive() && dual.getConfigManager().getSetting(Settings.INTERFACE_TERMINAL) == YesNo.YES) {
                        this.ifTrackerMap.put(ih, new InvTracker(dual, dual.getPatterns(), dual.getTermName()));
                    }
                }

                for (final IGridNode gn : this.grid.getMachines(PartInterface.class)) {
                    final IInterfaceHost ih = (IInterfaceHost) gn.getMachine();
                    final DualityInterface dual = ih.getInterfaceDuality();
                    if (gn.isActive() && dual.getConfigManager().getSetting(Settings.INTERFACE_TERMINAL) == YesNo.YES) {
                        this.ifTrackerMap.put(ih, new InvTracker(dual, dual.getPatterns(), dual.getTermName()));
                    }
                }

                for (final IGridNode gn : getEnhancedInterface()) {
                    final IInterfaceHost ih = (IInterfaceHost) gn.getMachine();
                    final DualityInterface dual = ih.getInterfaceDuality();
                    if (gn.isActive() && dual.getConfigManager().getSetting(Settings.INTERFACE_TERMINAL) == YesNo.YES) {
                        this.ifTrackerMap.put(ih, new InvTracker(dual, dual.getPatterns(), dual.getTermName()));
                    }

                }

            }
        }

        data.setBoolean("clear", true);

        for (final Map.Entry<IInterfaceHost, InvTracker> en : this.ifTrackerMap.entrySet()) {
            final InvTracker inv = en.getValue();
            this.idMap.put(inv.getWhich(), inv);
            this.addItems(data, inv, 0, inv.getServer().getSlots());
        }
    }

    private void addItems(final NBTTagCompound data, final InvTracker inv, final int offset, final int length) {
        final String name = '=' + Long.toString(inv.getWhich(), Character.MAX_RADIX);
        final NBTTagCompound tag = data.getCompoundTag(name);

        if (tag.isEmpty()) {
            tag.setLong("sortBy", inv.getSortBy());
            tag.setString("un", inv.getUnlocalizedName());
            tag.setInteger("tier", inv.getTier());
        }

        for (int x = 0; x < length; x++) {
            final NBTTagCompound itemNBT = new NBTTagCompound();

            final ItemStack is = inv.getServer().getStackInSlot(x + offset);

            // "update" client side.
            ItemHandlerUtil.setStackInSlot(inv.getClient(), x + offset, is.isEmpty() ? ItemStack.EMPTY : is.copy());

            if (!is.isEmpty()) {
                is.writeToNBT(itemNBT);
            }

            tag.setTag(Integer.toString(x + offset), itemNBT);
        }

        data.setTag(name, tag);
    }

    private Set<IGridNode> getEnhancedInterface() {
        Set<IGridNode> set = new HashSet<>();
        for (final IGridNode gn : grid.getMachines(TileEnhancedInterfaceTier1.class)) {
            set.add(gn);
        }
        for (IGridNode gn : grid.getMachines(TileEnhancedInterfaceTier2.class)) {
            set.add(gn);
        }
        for (final IGridNode gn : grid.getMachines(TileEnhancedInterfaceTier3.class)) {
            set.add(gn);
        }
        for (final IGridNode gn : grid.getMachines(PartEnhancedInterfaceTier1.class)) {
            set.add(gn);
        }
        for (final IGridNode gn : grid.getMachines(PartEnhancedInterfaceTier2.class)) {
            set.add(gn);
        }
        for (final IGridNode gn : grid.getMachines(PartEnhancedInterfaceTier3.class)) {
            set.add(gn);
        }
        return set;
    }

}


