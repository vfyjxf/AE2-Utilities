package com.github.vfyjxf.ae2utilities.block;

import appeng.api.util.AEPartLocation;
import appeng.api.util.IOrientable;
import appeng.block.AEBaseTileBlock;
import com.github.vfyjxf.ae2utilities.network.Ae2uGuiHandler;
import com.github.vfyjxf.ae2utilities.tile.TileEnhancedInterfaceBase;
import com.github.vfyjxf.ae2utilities.tile.TileEnhancedInterfaceTier1;
import com.github.vfyjxf.ae2utilities.tile.TileEnhancedInterfaceTier2;
import com.github.vfyjxf.ae2utilities.tile.TileEnhancedInterfaceTier3;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockEnhancedInterface extends AEBaseTileBlock {

    public static final int MAX_TIER = 3;
    private static final PropertyBool OMNIDIRECTIONAL = PropertyBool.create("omnidirectional");
    private static final PropertyDirection FACING = PropertyDirection.create("facing");
    private final int tier;

    public BlockEnhancedInterface(int tier) {
        super(Material.IRON);
        if (tier == 1) {
            setTileEntity(TileEnhancedInterfaceTier1.class);
        } else if (tier == 2) {
            setTileEntity(TileEnhancedInterfaceTier2.class);
        } else if (tier == 3) {
            setTileEntity(TileEnhancedInterfaceTier3.class);
        }

        this.tier = tier;
    }


    @Override
    protected IProperty[] getAEStates() {
        return new IProperty[]{OMNIDIRECTIONAL};
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, OMNIDIRECTIONAL, FACING);
    }

    @Nonnull
    @Override
    public IBlockState getActualState(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
        TileEnhancedInterfaceBase te = this.getTileEntity(world, pos);
        return te == null ? state
                : state.withProperty(OMNIDIRECTIONAL, te.isOmniDirectional()).withProperty(FACING, te.getForward());
    }

    @Override
    public boolean onActivated(final World world, final BlockPos pos, final EntityPlayer player, final EnumHand hand, final @Nullable ItemStack heldItem, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (player.isSneaking()) {
            return false;
        }

        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileEnhancedInterfaceBase) {

            if (!world.isRemote) {
                Ae2uGuiHandler.openGui(player, this.tier, tile, AEPartLocation.fromFacing(side));
            }

            return true;
        }

        return false;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        switch (this.tier) {
            case 2:
                return new TileEnhancedInterfaceTier2();
            case 3:
                return new TileEnhancedInterfaceTier3();
            case 1:
            default:
                return new TileEnhancedInterfaceTier1();
        }

    }

    @Override
    protected boolean hasCustomRotation() {
        return true;
    }

    @Override
    protected void customRotateBlock(final IOrientable rotatable, final EnumFacing axis) {
        if (rotatable instanceof TileEnhancedInterfaceBase) {
            ((TileEnhancedInterfaceBase) rotatable).setSide(axis);
        }
    }

}
