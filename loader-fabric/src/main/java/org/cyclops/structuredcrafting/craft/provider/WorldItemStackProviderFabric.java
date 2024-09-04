package org.cyclops.structuredcrafting.craft.provider;

import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * World that can provide an itemstack.
 * @author rubensworks
 */
public class WorldItemStackProviderFabric extends WorldItemStackProviderBase {

    @Override
    protected boolean hasEmptyItemHandler(Level world, BlockPos pos, Direction side) {
        Storage<ItemVariant> itemHandler = ItemStorage.SIDED.find(world, pos, side);
        if (itemHandler != null) {
            return !itemHandler.nonEmptyIterator().hasNext();
        }
        return true;
    }

    @Override
    public ItemStack getItemStack(Level world, BlockPos pos, Direction side) {
        BlockState blockState = world.getBlockState(pos);
        if(blockState != null && hasEmptyItemHandler(world, pos, side)) {
            return blockState.getBlock().getCloneItemStack(world, pos, blockState);
        }
        return ItemStack.EMPTY;
    }

}
