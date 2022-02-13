package com.github.vfyjxf.ae2utilities.proxy;

import appeng.block.AEBaseItemBlock;
import appeng.block.AEBaseTileBlock;
import appeng.core.features.ActivityState;
import appeng.core.features.BlockStackSrc;
import appeng.tile.AEBaseTile;
import com.github.vfyjxf.ae2utilities.AE2Utilities;
import com.github.vfyjxf.ae2utilities.block.BlockEnhancedInterface;
import com.github.vfyjxf.ae2utilities.item.ItemPartEnhancedInterfaceTier1;
import com.github.vfyjxf.ae2utilities.item.ItemPartEnhancedInterfaceTier2;
import com.github.vfyjxf.ae2utilities.item.ItemPartEnhancedInterfaceTier3;
import com.github.vfyjxf.ae2utilities.tile.TileEnhancedInterfaceTier1;
import com.github.vfyjxf.ae2utilities.tile.TileEnhancedInterfaceTier2;
import com.github.vfyjxf.ae2utilities.tile.TileEnhancedInterfaceTier3;
import com.github.vfyjxf.ae2utilities.utils.NameConstants;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = AE2Utilities.MODID)
public class RegistryHandler {

    public static final RegistryHandler INSTANCE = new RegistryHandler();

    private final List<Block> blocks = new ArrayList<>();
    private final List<Item> items = new ArrayList<>();

    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event) {
        for (Item item : INSTANCE.getItems()) {
            if (item != null) {
                event.getRegistry().register(item);
            }
        }
    }

    @SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> event) {
        for (Block block : INSTANCE.getBlocks()) {
            event.getRegistry().register(block);
        }
        GameRegistry.registerTileEntity(TileEnhancedInterfaceTier1.class, new ResourceLocation(AE2Utilities.MODID, "enhanced_interface_1"));
        GameRegistry.registerTileEntity(TileEnhancedInterfaceTier2.class, new ResourceLocation(AE2Utilities.MODID, "enhanced_interface_2"));
        GameRegistry.registerTileEntity(TileEnhancedInterfaceTier3.class, new ResourceLocation(AE2Utilities.MODID, "enhanced_interface_3"));
    }


    public void initBlocks() {
        for (int i = 1; i <= BlockEnhancedInterface.MAX_TIER; i++) {
            Block block = new BlockEnhancedInterface(i);
            String registryName = AE2Utilities.MODID + ":" + NameConstants.BASE_BLOCK_NAME + i;
            block.setRegistryName(registryName);
            block.setTranslationKey(AE2Utilities.MODID + "." + NameConstants.BASE_BLOCK_NAME + i);
            blocks.add(block);
            Item item = new AEBaseItemBlock(block).setRegistryName(registryName);
            items.add(item);
        }
    }

    public void initItems() {
        items.add(initItem(new ItemPartEnhancedInterfaceTier1(), NameConstants.BASE_ITEM_PART_INTERFACE_NAME + 1));
        items.add(initItem(new ItemPartEnhancedInterfaceTier2(), NameConstants.BASE_ITEM_PART_INTERFACE_NAME + 2));
        items.add(initItem(new ItemPartEnhancedInterfaceTier3(), NameConstants.BASE_ITEM_PART_INTERFACE_NAME + 3));
    }

    private Item initItem(Item item, String registryName) {
        item.setRegistryName(AE2Utilities.MODID + ":" + registryName);
        item.setTranslationKey(AE2Utilities.MODID + "." + registryName);
        return item;
    }

    public void onInit() {
        for (Block block : this.getBlocks()) {
            if (block instanceof AEBaseTileBlock) {
                AEBaseTile.registerTileItem(((AEBaseTileBlock) block).getTileEntityClass(), new BlockStackSrc(block, 0, ActivityState.Enabled));
            }
        }
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public List<Item> getItems() {
        return items;
    }
}
