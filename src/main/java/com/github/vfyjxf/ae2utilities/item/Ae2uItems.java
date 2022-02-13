package com.github.vfyjxf.ae2utilities.item;

import com.github.vfyjxf.ae2utilities.AE2Utilities;
import com.github.vfyjxf.ae2utilities.utils.NameConstants;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class Ae2uItems {

    @GameRegistry.ObjectHolder(AE2Utilities.MODID + ":" + NameConstants.BASE_ITEM_PART_INTERFACE_NAME + 1)
    public static ItemPartEnhancedInterfaceTier1 ITEM_PART_ENHANCED_INTERFACE_TIER_1;
    @GameRegistry.ObjectHolder(AE2Utilities.MODID + ":" + NameConstants.BASE_ITEM_PART_INTERFACE_NAME + 2)
    public static ItemPartEnhancedInterfaceTier2 ITEM_PART_ENHANCED_INTERFACE_TIER_2;
    @GameRegistry.ObjectHolder(AE2Utilities.MODID + ":" + NameConstants.BASE_ITEM_PART_INTERFACE_NAME + 3)
    public static ItemPartEnhancedInterfaceTier3 ITEM_PART_ENHANCED_INTERFACE_TIER_3;


}
