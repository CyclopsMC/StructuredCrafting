package org.cyclops.structuredcrafting.craft.provider;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.helper.InventoryHelpers;
import org.cyclops.cyclopscore.helper.TileHelpers;

/**
 * Inventory that can provide itemstacks.
 * @author rubensworks
 */
public class InventoryItemStackProvider implements IItemStackProvider {

    protected Pair<Integer, ItemStack> getFirstItem(IInventory inventory, EnumFacing side) {
        for(int slot = 0; slot < inventory.getSizeInventory(); slot++) {
            ItemStack itemStack = inventory.getStackInSlot(slot);
            if(itemStack != null) {
                return Pair.of(slot, itemStack);
            }
        }
        return null;
    }

    @Override
    public boolean isValidForResults(World world, BlockPos pos, EnumFacing side) {
        IInventory inventory = TileHelpers.getSafeTile(world, pos, IInventory.class);
        return inventory != null;
    }

    @Override
    public boolean hasItemStack(World world, BlockPos pos, EnumFacing side) {
        IInventory inventory = TileHelpers.getSafeTile(world, pos, IInventory.class);
        return inventory != null && getFirstItem(inventory, side) != null;
    }

    @Override
    public ItemStack getItemStack(World world, BlockPos pos, EnumFacing side) {
        IInventory inventory = TileHelpers.getSafeTile(world, pos, IInventory.class);
        return getFirstItem(inventory, side).getRight();
    }

    @Override
    public void reduceItemStack(World world, BlockPos pos, EnumFacing side) {
        IInventory inventory = TileHelpers.getSafeTile(world, pos, IInventory.class);
        Pair<Integer, ItemStack> result = getFirstItem(inventory, side);
        ItemStack newItemStack = result.getRight().copy();
        newItemStack.stackSize--;
        if(newItemStack.stackSize <= 0) {
            newItemStack = null;
        }
        inventory.setInventorySlotContents(result.getLeft(), newItemStack);
    }

    @Override
    public boolean addItemStack(World world, BlockPos pos, EnumFacing side, ItemStack itemStack) {
        IInventory inventory = TileHelpers.getSafeTile(world, pos, IInventory.class);
        for(int slot = 0; slot < inventory.getSizeInventory(); slot++) {
            if(InventoryHelpers.addToSlot(inventory, slot, itemStack)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setItemStack(World world, BlockPos pos, EnumFacing side, ItemStack itemStack) {
        IInventory inventory = TileHelpers.getSafeTile(world, pos, IInventory.class);
        Pair<Integer, ItemStack> result = getFirstItem(inventory, side);
        if(result != null) {
            inventory.setInventorySlotContents(result.getLeft(), itemStack);
            return true;
        }
        return false;
    }
}
