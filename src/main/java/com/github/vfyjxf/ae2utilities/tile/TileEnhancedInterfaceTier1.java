package com.github.vfyjxf.ae2utilities.tile;

import com.github.vfyjxf.ae2utilities.block.Ae2uBlocks;
import com.github.vfyjxf.ae2utilities.network.Ae2uGuiHandler;
import net.minecraft.item.ItemStack;

public class TileEnhancedInterfaceTier1 extends TileEnhancedInterfaceBase{
    @Override
    public int getTier() {
        return 1;
    }

    @Override
    public int getGuiId() {
        return Ae2uGuiHandler.GUI_ENHANCED_INTERFACE_TIER_1_ID;
    }

    @Override
    public ItemStack getItemStackRepresentation() {
        return new ItemStack(Ae2uBlocks.BLOCK_ENHANCED_INTERFACE_TIER_1);
    }
}
