package org.cyclops.structuredcrafting.craft.provider;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
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
    public boolean isValidForResults(World world, BlockPos pos, EnumFacing side) {
        return world.isAirBlock(pos);
    }

    protected boolean hasEmptyItemHandler(World world, BlockPos pos, EnumFacing side) {
        IItemHandler itemHandler = TileHelpers.getCapability(world, pos, side, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
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
    public boolean hasItemStack(World world, BlockPos pos, EnumFacing side) {
        return !world.isAirBlock(pos) && hasEmptyItemHandler(world, pos, side);
    }

    @Override
    public ItemStack getItemStack(World world, BlockPos pos, EnumFacing side) {
        IBlockState blockState = world.getBlockState(pos);

        ItemStack itemStack = ItemStack.EMPTY;
        if(blockState != null) {
            Item item = Item.getItemFromBlock(blockState.getBlock());
            if(item != null && hasEmptyItemHandler(world, pos, side)) {
                itemStack = new ItemStack(item, 1, blockState.getBlock().damageDropped(blockState));
            }
        }
        return itemStack;
    }

    @Override
    public void reduceItemStack(World world, BlockPos pos, EnumFacing side, boolean simulate) {
        if(!simulate) {
            world.setBlockToAir(pos);
        }
    }

    @Override
    public boolean addItemStack(World world, BlockPos pos, EnumFacing side, ItemStack itemStack, boolean simulate) {
        return setItemStack(world, pos, side, itemStack, simulate);
    }

    @Override
    public boolean setItemStack(World world, BlockPos pos, EnumFacing side, ItemStack itemStack, boolean simulate) {
        if(!simulate && itemStack.getItem() instanceof ItemBlock) {
            world.setBlockState(pos, ((ItemBlock) itemStack.getItem()).getBlock().getStateFromMeta(itemStack.getItemDamage()));
            itemStack.shrink(1);
        }
        if(!simulate && itemStack.getCount() > 0) {
            ItemStackHelpers.spawnItemStack(world, pos, itemStack);
        }
        return true;
    }
}
