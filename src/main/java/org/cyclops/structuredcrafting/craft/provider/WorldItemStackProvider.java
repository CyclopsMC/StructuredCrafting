package org.cyclops.structuredcrafting.craft.provider;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.cyclops.cyclopscore.helper.ItemStackHelpers;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.structuredcrafting.block.BlockStructuredCrafterConfig;

/**
 * World that can provide an itemstack.
 * @author rubensworks
 */
public class WorldItemStackProvider implements IItemStackProvider {
    @Override
    public boolean canProvideInput() {
        return BlockStructuredCrafterConfig.canTakeInputsFromWorld;
    }

    @Override
    public boolean canHandleOutput() {
        return BlockStructuredCrafterConfig.canPlaceOutputsIntoWorld;
    }

    @Override
    public boolean isValidForResults(World world, BlockPos pos, Direction side) {
        return world.isEmptyBlock(pos);
    }

    protected boolean hasEmptyItemHandler(World world, BlockPos pos, Direction side) {
        IItemHandler itemHandler = TileHelpers.getCapability(world, pos, side, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
        boolean emptyItemHandler = true;
        if (itemHandler != null) {
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                if (!itemHandler.extractItem(i, 1, true).isEmpty()) {
                    emptyItemHandler = false;
                    break;
                }
            }
        }
        return emptyItemHandler;
    }

    @Override
    public boolean hasItemStack(World world, BlockPos pos, Direction side) {
        return !world.isEmptyBlock(pos) && hasEmptyItemHandler(world, pos, side);
    }

    @Override
    public ItemStack getItemStack(World world, BlockPos pos, Direction side) {
        BlockState blockState = world.getBlockState(pos);

        ItemStack itemStack = ItemStack.EMPTY;
        if(blockState != null) {
            Item item = Item.byBlock(blockState.getBlock());
            if(item != null && hasEmptyItemHandler(world, pos, side)) {
                itemStack = new ItemStack(item, 1);
            }
        }
        return itemStack;
    }

    @Override
    public void reduceItemStack(World world, BlockPos pos, Direction side, boolean simulate) {
        if(!simulate) {
            world.removeBlock(pos, false);
        }
    }

    @Override
    public boolean addItemStack(World world, BlockPos pos, Direction side, ItemStack itemStack, boolean simulate) {
        return setItemStack(world, pos, side, itemStack, simulate);
    }

    @Override
    public boolean setItemStack(World world, BlockPos pos, Direction side, ItemStack itemStack, boolean simulate) {
        if(!simulate && itemStack.getItem() instanceof BlockItem) {
            world.setBlockAndUpdate(pos, ((BlockItem) itemStack.getItem()).getBlock().defaultBlockState());
            itemStack.shrink(1);
        }
        if(!simulate && itemStack.getCount() > 0) {
            ItemStackHelpers.spawnItemStack(world, pos, itemStack);
        }
        return true;
    }
}
