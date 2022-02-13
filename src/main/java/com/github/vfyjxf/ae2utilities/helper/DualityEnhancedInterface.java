package com.github.vfyjxf.ae2utilities.helper;

import appeng.helpers.DualityInterface;
import appeng.helpers.IInterfaceHost;
import appeng.me.helpers.AENetworkProxy;
import appeng.tile.inventory.AppEngInternalInventory;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class DualityEnhancedInterface extends DualityInterface {

    private final int tier;

    public DualityEnhancedInterface(AENetworkProxy networkProxy, IInterfaceHost ih, int tier) {
        super(networkProxy, ih);
        this.tier = tier;
        setPatterns();
    }

    private void setPatterns() {
        ObfuscationReflectionHelper.setPrivateValue(
                DualityInterface.class,
                this,
                new AppEngInternalInventory(this, (this.tier + 1) * NUMBER_OF_PATTERN_SLOTS),
                "patterns"
        );
    }

    public int getTier() {
        return tier;
    }
}
