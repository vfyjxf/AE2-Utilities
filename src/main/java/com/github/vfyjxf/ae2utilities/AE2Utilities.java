package com.github.vfyjxf.ae2utilities;

import com.github.vfyjxf.ae2utilities.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = AE2Utilities.MODID,
        name = AE2Utilities.MOD_NAME,
        version = AE2Utilities.VERSION,
        dependencies = AE2Utilities.DEPENDENCIES,
        useMetadata = true)
public class AE2Utilities {

    public static final String MODID = "ae2utilities";
    public static final String MOD_NAME = "AE2 Utilities";
    public static final String VERSION = "@VERSION@";
    public static final String DEPENDENCIES = "required-after:appliedenergistics2;required-after:mixinbooter;";
    public static final Logger logger = LogManager.getLogger("AE2Utilities");

    @Mod.Instance(MODID)
    public static AE2Utilities instance;

    @SidedProxy(clientSide = "com.github.vfyjxf.ae2utilities.proxy.ClientProxy", serverSide = "com.github.vfyjxf.ae2utilities.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

}
