package com.github.vfyjxf.ae2utilities.tile;

import com.github.vfyjxf.ae2utilities.block.Ae2uBlocks;
import com.github.vfyjxf.ae2utilities.network.Ae2uGuiHandler;
import net.minecraft.item.ItemStack;

public class TileEnhancedInterfaceTier2 extends TileEnhancedInterfaceBase{
    @Override
    public int getTier() {
        return 2;
    }

    @Override
    public int getGuiId() {
        return Ae2uGuiHandler.GUI_ENHANCED_INTERFACE_TIER_2_ID;
    }

    @Override
    public ItemStack getItemStackRepresentation() {
        return new ItemStack(Ae2uBlocks.BLOCK_ENHANCED_INTERFACE_TIER_2);
    }
}
