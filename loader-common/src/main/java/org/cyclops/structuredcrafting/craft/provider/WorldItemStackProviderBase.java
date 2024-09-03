package org.cyclops.structuredcrafting.craft.provider;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.structuredcrafting.block.BlockStructuredCrafterConfig;

import java.util.UUID;

/**
 * World that can provide an itemstack.
 * @author rubensworks
 */
public abstract class WorldItemStackProviderBase implements IItemStackProvider {

    protected static GameProfile PROFILE = new GameProfile(UUID.fromString("41C82C87-7AfB-4024-BB57-13D2C99CAE78"), "[StructuredCrafting]");

    @Override
    public boolean canProvideInput() {
        return BlockStructuredCrafterConfig.canTakeInputsFromWorld;
    }

    @Override
    public boolean canHandleOutput() {
        return BlockStructuredCrafterConfig.canPlaceOutputsIntoWorld;
    }

    @Override
    public boolean isValidForResults(Level world, BlockPos pos, Direction side) {
        return world.isEmptyBlock(pos);
    }

    protected abstract boolean hasEmptyItemHandler(Level world, BlockPos pos, Direction side);

    @Override
    public boolean hasItemStack(Level world, BlockPos pos, Direction side) {
        return !world.isEmptyBlock(pos) && hasEmptyItemHandler(world, pos, side);
    }

    @Override
    public boolean reduceItemStack(Level world, BlockPos pos, Direction side, boolean simulate) {
        boolean wasAir = world.getBlockState(pos).isAir();
        if(!simulate) {
            world.removeBlock(pos, false);
        }
        return !wasAir;
    }

    @Override
    public boolean addItemStack(Level world, BlockPos pos, Direction side, ItemStack itemStack, boolean simulate) {
        return setItemStack(world, pos, side, itemStack, simulate);
    }

    @Override
    public boolean setItemStack(Level world, BlockPos pos, Direction side, ItemStack itemStack, boolean simulate) {
        if(!simulate && itemStack.getItem() instanceof BlockItem) {
            world.setBlockAndUpdate(pos, ((BlockItem) itemStack.getItem()).getBlock().defaultBlockState());
            itemStack.shrink(1);
        }
        if(!simulate && itemStack.getCount() > 0) {
            IModHelpers.get().getItemStackHelpers().spawnItemStack(world, pos, itemStack);
        }
        return true;
    }
}
