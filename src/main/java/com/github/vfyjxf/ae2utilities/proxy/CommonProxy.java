package com.github.vfyjxf.ae2utilities.proxy;

import com.github.vfyjxf.ae2utilities.AE2Utilities;
import com.github.vfyjxf.ae2utilities.network.Ae2uGuiHandler;
import com.github.vfyjxf.ae2utilities.network.Ae2uNetworkHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        RegistryHandler.INSTANCE.initBlocks();
        RegistryHandler.INSTANCE.initItems();
        Ae2uNetworkHandler.init();
    }

    public void init(FMLInitializationEvent event) {
        RegistryHandler.INSTANCE.onInit();
    }

    public void postInit(FMLPostInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(AE2Utilities.instance, new Ae2uGuiHandler());
    }

}
