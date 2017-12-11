package org.cyclops.structuredcrafting.craft.provider;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
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
            if(!itemStack.isEmpty()) {
                return Pair.of(slot, itemStack);
            }
        }
        return null;
    }

    protected Pair<Integer, ItemStack> getFirstItem(IItemHandler itemHandler, EnumFacing side) {
        for(int slot = 0; slot < itemHandler.getSlots(); slot++) {
            ItemStack itemStack = itemHandler.extractItem(slot, 1, true);
            if(!itemStack.isEmpty()) {
                return Pair.of(slot, itemStack);
            }
        }
        return null;
    }

    @Override
    public boolean isValidForResults(World world, BlockPos pos, EnumFacing side) {
        IItemHandler itemHandler = TileHelpers.getCapability(world, pos, side, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
        IInventory inventory = TileHelpers.getSafeTile(world, pos, IInventory.class);
        return itemHandler != null || inventory != null;
    }

    @Override
    public boolean hasItemStack(World world, BlockPos pos, EnumFacing side) {
        IInventory inventory = TileHelpers.getSafeTile(world, pos, IInventory.class);
        IItemHandler itemHandler = TileHelpers.getCapability(world, pos, side, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
        return itemHandler != null || inventory != null;
    }

    @Override
    public ItemStack getItemStack(World world, BlockPos pos, EnumFacing side) {
        IItemHandler itemHandler = TileHelpers.getCapability(world, pos, side, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
        IInventory inventory = TileHelpers.getSafeTile(world, pos, IInventory.class);
        Pair<Integer, ItemStack> result = itemHandler != null ? getFirstItem(itemHandler, side) : getFirstItem(inventory, side);
        if (result != null) {
            return result.getRight();
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void reduceItemStack(World world, BlockPos pos, EnumFacing side, boolean simulate) {
        IItemHandler itemHandler = TileHelpers.getCapability(world, pos, side, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
        if(itemHandler != null) {
            for(int slot = 0; slot < itemHandler.getSlots(); slot++) {
                if(!itemHandler.extractItem(slot, 1, simulate).isEmpty()) {
                    break;
                }
            }
        } else {
            IInventory inventory = TileHelpers.getSafeTile(world, pos, IInventory.class);
            Pair<Integer, ItemStack> result = getFirstItem(inventory, side);
            ItemStack newItemStack = result.getRight().copy();
            newItemStack.shrink(1);
            if (newItemStack.getCount() <= 0) {
                newItemStack = ItemStack.EMPTY;
            }
            if(!simulate) {
                inventory.setInventorySlotContents(result.getLeft(), newItemStack);
            }
        }
    }

    @Override
    public boolean addItemStack(World world, BlockPos pos, EnumFacing side, ItemStack itemStack, boolean simulate) {
        IItemHandler itemHandler = TileHelpers.getCapability(world, pos, side, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
        if(itemHandler != null) {
            for(int slot = 0; slot < itemHandler.getSlots(); slot++) {
                if(itemHandler.insertItem(slot, itemStack, simulate).isEmpty()) {
                    return true;
                }
            }
        } else {
            IInventory inventory = TileHelpers.getSafeTile(world, pos, IInventory.class);
            for (int slot = 0; slot < inventory.getSizeInventory(); slot++) {
                if (InventoryHelpers.addToSlot(inventory, slot, itemStack, simulate)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean setItemStack(World world, BlockPos pos, EnumFacing side, ItemStack itemStack, boolean simulate) {
        IItemHandler itemHandler = TileHelpers.getCapability(world, pos, side, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
        if(itemHandler != null) {
            for(int slot = 0; slot < itemHandler.getSlots(); slot++) {
                if(itemHandler.insertItem(slot, itemStack, simulate).isEmpty()) {
                    return true;
                }
            }
        } else {
            IInventory inventory = TileHelpers.getSafeTile(world, pos, IInventory.class);
            Pair<Integer, ItemStack> result = getFirstItem(inventory, side);
            if (result != null) {
                if(!simulate) {
                    inventory.setInventorySlotContents(result.getLeft(), itemStack);
                }
                return true;
            }
        }
        return false;
    }
}
