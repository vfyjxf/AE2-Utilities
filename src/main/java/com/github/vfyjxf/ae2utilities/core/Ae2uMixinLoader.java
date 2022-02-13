package com.github.vfyjxf.ae2utilities.core;

import org.spongepowered.asm.mixin.Mixins;
import zone.rong.mixinbooter.MixinLoader;

@MixinLoader
public class Ae2uMixinLoader {

    {
        Mixins.addConfiguration("mixins.ae2utilities.json");
    }

}
