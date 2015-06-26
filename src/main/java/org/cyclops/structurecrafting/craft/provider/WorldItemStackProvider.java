package org.cyclops.structurecrafting.craft.provider;

import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.helper.ItemStackHelpers;

/**
 * World that can provide an itemstack.
 * @author rubensworks
 */
public class WorldItemStackProvider implements IItemStackProvider {
    @Override
    public boolean isValidForResults(World world, BlockPos pos, EnumFacing side) {
        return world.isAirBlock(pos);
    }

    @Override
    public boolean hasItemStack(World world, BlockPos pos, EnumFacing side) {
        return !world.isAirBlock(pos);
    }

    @Override
    public ItemStack getItemStack(World world, BlockPos pos, EnumFacing side) {
        IBlockState blockState = world.getBlockState(pos);

        ItemStack itemStack = null;
        if(blockState != null) {
            Item item = blockState.getBlock().getItem(world, pos);
            if(item != null) {
                itemStack = new ItemStack(item, blockState.getBlock().getDamageValue(world, pos));
            }
        }
        return itemStack;
    }

    @Override
    public void reduceItemStack(World world, BlockPos pos, EnumFacing side) {
        world.setBlockToAir(pos);
    }

    @Override
    public boolean addItemStack(World world, BlockPos pos, EnumFacing side, ItemStack itemStack) {
        return setItemStack(world, pos, side, itemStack);
    }

    @Override
    public boolean setItemStack(World world, BlockPos pos, EnumFacing side, ItemStack itemStack) {
        if(itemStack.getItem() instanceof ItemBlock) {
            world.setBlockState(pos, ((ItemBlock) itemStack.getItem()).getBlock().getStateFromMeta(itemStack.getItemDamage()));
        } else {
            ItemStackHelpers.spawnItemStack(world, pos, itemStack);
        }
        return true;
    }
}
