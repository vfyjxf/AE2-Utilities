package com.github.vfyjxf.ae2utilities.mixins;

import appeng.api.networking.crafting.ICraftingPatternDetails;
import appeng.api.networking.crafting.ICraftingProvider;
import appeng.api.networking.events.MENetworkCraftingPatternChange;
import appeng.helpers.DualityInterface;
import appeng.me.GridAccessException;
import appeng.me.helpers.AENetworkProxy;
import appeng.tile.inventory.AppEngInternalInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Iterator;
import java.util.List;

/**
 * @author vfyjxf
 */
@Mixin(value = DualityInterface.class, remap = false)
public abstract class MixinDualityEnhancedInterface {

    @Shadow
    public abstract IItemHandler getPatterns();

    @Shadow
    @Final
    private AENetworkProxy gridProxy;

    @Shadow
    private List<ICraftingPatternDetails> craftingList;

    @Shadow
    protected abstract void addToCraftingList(ItemStack is);

    @Shadow @Final private AppEngInternalInventory patterns;

    /**
     * @reason Enables samples greater than 9 to be submitted to the network
     * @author vfyjxf
     */
    @Overwrite
    private void updateCraftingList() {
        final boolean[] accountedFor = new boolean[patterns.getSlots()];

        assert (accountedFor.length == this.getPatterns().getSlots());

        if (!this.gridProxy.isReady()) {
            return;
        }

        if (this.craftingList != null) {
            final Iterator<ICraftingPatternDetails> i = this.craftingList.iterator();
            while (i.hasNext()) {
                final ICraftingPatternDetails details = i.next();
                boolean found = false;

                for (int x = 0; x < accountedFor.length; x++) {
                    final ItemStack is = this.getPatterns().getStackInSlot(x);
                    if (details.getPattern() == is) {
                        accountedFor[x] = found = true;
                    }
                }

                if (!found) {
                    i.remove();
                }
            }
        }

        for (int x = 0; x < accountedFor.length; x++) {
            if (!accountedFor[x]) {
                this.addToCraftingList(this.getPatterns().getStackInSlot(x));
            }
        }

        try {
            this.gridProxy.getGrid().postEvent(new MENetworkCraftingPatternChange((ICraftingProvider) this, this.gridProxy.getNode()));
        } catch (final GridAccessException e) {
            // :P
        }
    }


}
