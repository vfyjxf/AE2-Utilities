package com.github.vfyjxf.ae2utilities.item;

import appeng.api.AEApi;
import appeng.api.parts.IPartItem;
import com.github.vfyjxf.ae2utilities.parts.PartEnhancedInterfaceTier3;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemPartEnhancedInterfaceTier3 extends Item implements IPartItem<PartEnhancedInterfaceTier3> {

    @Nullable
    @Override
    public PartEnhancedInterfaceTier3 createPartFromItemStack(ItemStack is) {
        return new PartEnhancedInterfaceTier3(is);
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUse(@Nonnull EntityPlayer player, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumHand hand, @Nonnull EnumFacing side, float hitX, float hitY, float hitZ) {
        return AEApi.instance().partHelper().placeBus(player.getHeldItem(hand), pos, side, player, hand, world);
    }

}
