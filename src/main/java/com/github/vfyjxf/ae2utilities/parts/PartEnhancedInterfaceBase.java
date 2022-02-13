package com.github.vfyjxf.ae2utilities.parts;

import appeng.api.config.Actionable;
import appeng.api.config.Upgrades;
import appeng.api.networking.IGridNode;
import appeng.api.networking.crafting.ICraftingLink;
import appeng.api.networking.crafting.ICraftingPatternDetails;
import appeng.api.networking.crafting.ICraftingProviderHelper;
import appeng.api.networking.events.MENetworkChannelsChanged;
import appeng.api.networking.events.MENetworkEventSubscribe;
import appeng.api.networking.events.MENetworkPowerStatusChange;
import appeng.api.networking.ticking.IGridTickable;
import appeng.api.networking.ticking.TickRateModulation;
import appeng.api.networking.ticking.TickingRequest;
import appeng.api.parts.IPartCollisionHelper;
import appeng.api.parts.IPartModel;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.IStorageChannel;
import appeng.api.storage.IStorageMonitorable;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IAEStack;
import appeng.api.util.AECableType;
import appeng.api.util.IConfigManager;
import appeng.core.sync.GuiBridge;
import appeng.helpers.DualityInterface;
import appeng.helpers.IInterfaceHost;
import appeng.helpers.IPriorityHost;
import appeng.helpers.Reflected;
import appeng.parts.PartBasicState;
import appeng.util.Platform;
import appeng.util.inv.IAEAppEngInventory;
import appeng.util.inv.IInventoryDestination;
import appeng.util.inv.InvOperation;
import com.github.vfyjxf.ae2utilities.helper.DualityEnhancedInterface;
import com.github.vfyjxf.ae2utilities.network.Ae2uGuiHandler;
import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.EnumSet;
import java.util.List;

public abstract class PartEnhancedInterfaceBase extends PartBasicState implements IGridTickable, IStorageMonitorable, IInventoryDestination, IInterfaceHost, IAEAppEngInventory, IPriorityHost {


    private final DualityEnhancedInterface duality;

    @Reflected
    public PartEnhancedInterfaceBase(ItemStack is) {
        super(is);
        duality = new DualityEnhancedInterface(this.getProxy(), this, this.getTier());
    }

    @MENetworkEventSubscribe
    public void stateChange(final MENetworkChannelsChanged c) {
        this.duality.notifyNeighbors();
    }

    @MENetworkEventSubscribe
    public void stateChange(final MENetworkPowerStatusChange c) {
        this.duality.notifyNeighbors();
    }


    @Override
    public void getBoxes(IPartCollisionHelper bch) {
        bch.addBox(2, 2, 14, 14, 14, 16);
        bch.addBox(5, 5, 12, 11, 11, 14);
    }

    @Override
    public int getInstalledUpgrades(final Upgrades u) {
        return this.duality.getInstalledUpgrades(u);
    }


    @Override
    public void gridChanged() {
        this.duality.gridChanged();
    }

    @Override
    public void readFromNBT(final NBTTagCompound data) {
        super.readFromNBT(data);
        this.duality.readFromNBT(data);
    }

    @Override
    public void writeToNBT(final NBTTagCompound data) {
        super.writeToNBT(data);
        this.duality.writeToNBT(data);
    }

    @Override
    public void addToWorld() {
        super.addToWorld();
        this.duality.initialize();
    }


    @Override
    public void getDrops(final List<ItemStack> drops, final boolean wrenched) {
        this.duality.addDrops(drops);
    }

    @Override
    public float getCableConnectionLength(AECableType cable) {
        return 4;
    }

    @Override
    public IConfigManager getConfigManager() {
        return this.duality.getConfigManager();
    }

    @Override
    public IItemHandler getInventoryByName(final String name) {
        return this.duality.getInventoryByName(name);
    }

    @Override
    public boolean onPartActivate(final EntityPlayer p, final EnumHand hand, final Vec3d pos) {
        if (Platform.isServer()) {
            Ae2uGuiHandler.openGui(p, this.getTier(), this.getTileEntity(), this.getSide());
        }
        return true;
    }

    @Override
    public boolean canInsert(final ItemStack stack) {
        return this.duality.canInsert(stack);
    }

    @Override
    public <T extends IAEStack<T>> IMEMonitor<T> getInventory(IStorageChannel<T> channel) {
        return this.duality.getInventory(channel);
    }

    @Nonnull
    @Override
    public TickingRequest getTickingRequest(@Nonnull final IGridNode node) {
        return this.duality.getTickingRequest(node);
    }

    @Nonnull
    @Override
    public TickRateModulation tickingRequest(@Nonnull final IGridNode node, final int ticksSinceLastCall) {
        return this.duality.tickingRequest(node, ticksSinceLastCall);
    }

    @Override
    public void onChangeInventory(final IItemHandler inv, final int slot, final InvOperation mc, final ItemStack removedStack, final ItemStack newStack) {
        this.duality.onChangeInventory(inv, slot, mc, removedStack, newStack);
    }


    @Override
    public DualityInterface getInterfaceDuality() {
        return this.duality;
    }

    @Override
    public EnumSet<EnumFacing> getTargets() {
        return EnumSet.of(this.getSide().getFacing());
    }

    @Override
    public TileEntity getTileEntity() {
        return super.getHost().getTile();
    }

    @Override
    public boolean pushPattern(final ICraftingPatternDetails patternDetails, final InventoryCrafting table) {
        return this.duality.pushPattern(patternDetails, table);
    }


    @Override
    public boolean isBusy() {
        return this.duality.isBusy();
    }

    @Override
    public void provideCrafting(final ICraftingProviderHelper craftingTracker) {
        this.duality.provideCrafting(craftingTracker);
    }


    @Override
    public ImmutableSet<ICraftingLink> getRequestedJobs() {
        return this.duality.getRequestedJobs();
    }

    @Override
    public IAEItemStack injectCraftedItems(final ICraftingLink link, final IAEItemStack items, final Actionable mode) {
        return this.duality.injectCraftedItems(link, items, mode);
    }

    @Override
    public void jobStateChange(final ICraftingLink link) {
        this.duality.jobStateChange(link);
    }

    @Override
    public int getPriority() {
        return this.duality.getPriority();
    }

    @Override
    public void setPriority(final int newValue) {
        this.duality.setPriority(newValue);
    }

    @Nonnull
    @Override
    public abstract IPartModel getStaticModels();

    @Override
    public boolean hasCapability(Capability<?> capabilityClass) {
        return this.duality.hasCapability(capabilityClass, this.getSide().getFacing());
    }

    @Override
    public <T> T getCapability(Capability<T> capabilityClass) {
        return this.duality.getCapability(capabilityClass, this.getSide().getFacing());
    }

    @Override
    public abstract ItemStack getItemStackRepresentation();

    @Override
    public GuiBridge getGuiBridge() {
        return GuiBridge.GUI_Handler;
    }

    public abstract int getTier();

}
