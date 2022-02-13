package com.github.vfyjxf.ae2utilities.utils;

import appeng.helpers.DualityInterface;
import appeng.tile.inventory.AppEngInternalInventory;
import com.github.vfyjxf.ae2utilities.helper.DualityEnhancedInterface;
import net.minecraftforge.items.IItemHandler;

/**
 *
 */
public class InvTracker {
    private static long autoBase = Long.MIN_VALUE;

    private final long sortBy;
    private final long which = autoBase++;
    private final String unlocalizedName;
    private final IItemHandler client;
    private final IItemHandler server;
    private final int tier;

    public InvTracker(final DualityInterface dual, final IItemHandler patterns, final String unlocalizedName) {
        this.server = patterns;
        this.client = new AppEngInternalInventory(null, this.server.getSlots());
        this.unlocalizedName = unlocalizedName;
        this.sortBy = dual.getSortValue();
        this.tier = dual instanceof DualityEnhancedInterface ? ((DualityEnhancedInterface) dual).getTier() : 0;
    }

    public long getSortBy() {
        return sortBy;
    }

    public long getWhich() {
        return which;
    }

    public String getUnlocalizedName() {
        return unlocalizedName;
    }

    public IItemHandler getClient() {
        return client;
    }

    public IItemHandler getServer() {
        return server;
    }

    public int getTier() {
        return tier;
    }
}
