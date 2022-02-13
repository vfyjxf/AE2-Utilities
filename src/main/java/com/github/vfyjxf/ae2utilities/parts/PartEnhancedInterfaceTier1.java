package com.github.vfyjxf.ae2utilities.parts;

import appeng.api.parts.IPartModel;
import appeng.helpers.Reflected;
import appeng.items.parts.PartModels;
import appeng.parts.PartModel;
import com.github.vfyjxf.ae2utilities.AE2Utilities;
import com.github.vfyjxf.ae2utilities.item.Ae2uItems;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class PartEnhancedInterfaceTier1 extends PartEnhancedInterfaceBase {

    @PartModels
    public static ResourceLocation[] MODELS = new ResourceLocation[] {
            new ResourceLocation(AE2Utilities.MODID, "part/enhanced_interface_base_tier1"),
            new ResourceLocation(AE2Utilities.MODID, "part/enhanced_interface_on"),
            new ResourceLocation(AE2Utilities.MODID, "part/enhanced_interface_off"),
            new ResourceLocation(AE2Utilities.MODID, "part/enhanced_interface_has_channel")
    };

    public static final PartModel MODELS_OFF = new PartModel(MODELS[0], MODELS[2]);
    public static final PartModel MODELS_ON = new PartModel(MODELS[0], MODELS[1]);
    public static final PartModel MODELS_HAS_CHANNEL = new PartModel(MODELS[0], MODELS[3]);

    @Reflected
    public PartEnhancedInterfaceTier1(ItemStack is) {
        super(is);
    }

    @Nonnull
    @Override
    public IPartModel getStaticModels() {
        if (this.isActive() && this.isPowered()) {
            return MODELS_HAS_CHANNEL;
        } else if (this.isPowered()) {
            return MODELS_ON;
        } else {
            return MODELS_OFF;
        }
    }

    @Override
    public ItemStack getItemStackRepresentation() {
        return new ItemStack(Ae2uItems.ITEM_PART_ENHANCED_INTERFACE_TIER_1);
    }

    @Override
    public int getTier() {
        return 1;
    }
}
