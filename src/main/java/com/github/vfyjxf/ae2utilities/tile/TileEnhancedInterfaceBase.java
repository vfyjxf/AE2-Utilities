package com.github.vfyjxf.ae2utilities.tile;

import appeng.core.sync.GuiBridge;
import appeng.tile.misc.TileInterface;
import com.github.vfyjxf.ae2utilities.helper.DualityEnhancedInterface;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public abstract class TileEnhancedInterfaceBase extends TileInterface {

    public TileEnhancedInterfaceBase() {
        setDuality(this, new DualityEnhancedInterface(this.getProxy(), this, getTier()));
    }

    private void setDuality(TileInterface tile, DualityEnhancedInterface duality) {
        ObfuscationReflectionHelper.setPrivateValue(TileInterface.class, tile, duality, "duality");
    }

    @Override
    public GuiBridge getGuiBridge() {
        return GuiBridge.GUI_Handler;
    }

    public abstract int getTier();

    public abstract int getGuiId();

    @Override
    public abstract ItemStack getItemStackRepresentation();

}
