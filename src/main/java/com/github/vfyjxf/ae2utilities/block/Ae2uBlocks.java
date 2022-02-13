package com.github.vfyjxf.ae2utilities.block;

import com.github.vfyjxf.ae2utilities.AE2Utilities;
import com.github.vfyjxf.ae2utilities.utils.NameConstants;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class Ae2uBlocks {

    @GameRegistry.ObjectHolder(AE2Utilities.MODID + ":" + NameConstants.BASE_BLOCK_NAME + 1)
    public static BlockEnhancedInterface BLOCK_ENHANCED_INTERFACE_TIER_1;
    @GameRegistry.ObjectHolder(AE2Utilities.MODID + ":" + NameConstants.BASE_BLOCK_NAME + 2)
    public static BlockEnhancedInterface BLOCK_ENHANCED_INTERFACE_TIER_2;
    @GameRegistry.ObjectHolder(AE2Utilities.MODID + ":" + NameConstants.BASE_BLOCK_NAME + 3)
    public static BlockEnhancedInterface BLOCK_ENHANCED_INTERFACE_TIER_3;

}
