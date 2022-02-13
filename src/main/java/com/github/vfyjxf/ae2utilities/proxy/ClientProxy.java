package com.github.vfyjxf.ae2utilities.proxy;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import static com.github.vfyjxf.ae2utilities.AE2Utilities.MODID;

@Mod.EventBusSubscriber(modid = MODID, value = Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent event) {
        for (Item item : RegistryHandler.INSTANCE.getItems()) {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
        }

    }

}
