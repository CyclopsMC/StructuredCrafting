package org.cyclops.structuredcrafting.craft.provider;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.helper.InventoryHelpers;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.structuredcrafting.block.BlockStructuredCrafterConfig;

/**
 * Inventory that can provide itemstacks.
 * @author rubensworks
 */
public class InventoryItemStackProvider implements IItemStackProvider {

    protected Pair<Integer, ItemStack> getFirstItem(IInventory inventory, Direction side) {
        for(int slot = 0; slot < inventory.getContainerSize(); slot++) {
            ItemStack itemStack = inventory.getItem(slot);
            if(!itemStack.isEmpty()) {
                return Pair.of(slot, itemStack);
            }
        }
        return null;
    }

    protected Pair<Integer, ItemStack> getFirstItem(IItemHandler itemHandler, Direction side) {
        for(int slot = 0; slot < itemHandler.getSlots(); slot++) {
            ItemStack itemStack = itemHandler.extractItem(slot, 1, true);
            if(!itemStack.isEmpty()) {
                return Pair.of(slot, itemStack);
            }
        }
        return null;
    }

    @Override
    public boolean canProvideInput() {
        return BlockStructuredCrafterConfig.canTakeInputsFromInventory;
    }

    @Override
    public boolean canHandleOutput() {
        return BlockStructuredCrafterConfig.canPlaceOutputsIntoInventory;
    }

    @Override
    public boolean isValidForResults(World world, BlockPos pos, Direction side) {
        IItemHandler itemHandler = TileHelpers.getCapability(world, pos, side, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
        IInventory inventory = TileHelpers.getSafeTile(world, pos, IInventory.class).orElse(null);
        return itemHandler != null || inventory != null;
    }

    @Override
    public boolean hasItemStack(World world, BlockPos pos, Direction side) {
        IInventory inventory = TileHelpers.getSafeTile(world, pos, IInventory.class).orElse(null);
        IItemHandler itemHandler = TileHelpers.getCapability(world, pos, side, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
        return itemHandler != null || inventory != null;
    }

    @Override
    public ItemStack getItemStack(World world, BlockPos pos, Direction side) {
        IItemHandler itemHandler = TileHelpers.getCapability(world, pos, side, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
        IInventory inventory = TileHelpers.getSafeTile(world, pos, IInventory.class).orElse(null);
        Pair<Integer, ItemStack> result = itemHandler != null ? getFirstItem(itemHandler, side) : getFirstItem(inventory, side);
        if (result != null) {
            return result.getRight();
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void reduceItemStack(World world, BlockPos pos, Direction side, boolean simulate) {
        IItemHandler itemHandler = TileHelpers.getCapability(world, pos, side, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
        if(itemHandler != null) {
            for(int slot = 0; slot < itemHandler.getSlots(); slot++) {
                if(!itemHandler.extractItem(slot, 1, simulate).isEmpty()) {
                    break;
                }
            }
        } else {
            IInventory inventory = TileHelpers.getSafeTile(world, pos, IInventory.class).orElse(null);
            Pair<Integer, ItemStack> result = getFirstItem(inventory, side);
            ItemStack newItemStack = result.getRight().copy();
            newItemStack.shrink(1);
            if (newItemStack.getCount() <= 0) {
                newItemStack = ItemStack.EMPTY;
            }
            if(!simulate) {
                inventory.setItem(result.getLeft(), newItemStack);
            }
        }
    }

    @Override
    public boolean addItemStack(World world, BlockPos pos, Direction side, ItemStack itemStack, boolean simulate) {
        IItemHandler itemHandler = TileHelpers.getCapability(world, pos, side, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
        if(itemHandler != null) {
            for(int slot = 0; slot < itemHandler.getSlots(); slot++) {
                if(itemHandler.insertItem(slot, itemStack, simulate).isEmpty()) {
                    return true;
                }
            }
        } else {
            IInventory inventory = TileHelpers.getSafeTile(world, pos, IInventory.class).orElse(null);
            for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
                if (InventoryHelpers.addToSlot(inventory, slot, itemStack, simulate)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean setItemStack(World world, BlockPos pos, Direction side, ItemStack itemStack, boolean simulate) {
        IItemHandler itemHandler = TileHelpers.getCapability(world, pos, side, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
        if(itemHandler != null) {
            for(int slot = 0; slot < itemHandler.getSlots(); slot++) {
                if(itemHandler.insertItem(slot, itemStack, simulate).isEmpty()) {
                    return true;
                }
            }
        } else {
            IInventory inventory = TileHelpers.getSafeTile(world, pos, IInventory.class).orElse(null);
            Pair<Integer, ItemStack> result = getFirstItem(inventory, side);
            if (result != null) {
                if(!simulate) {
                    inventory.setItem(result.getLeft(), itemStack);
                }
                return true;
            }
        }
        return false;
    }
}
