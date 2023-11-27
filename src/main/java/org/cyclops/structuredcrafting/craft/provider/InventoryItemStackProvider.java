package org.cyclops.structuredcrafting.craft.provider;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.helper.BlockEntityHelpers;
import org.cyclops.cyclopscore.helper.InventoryHelpers;
import org.cyclops.structuredcrafting.block.BlockStructuredCrafterConfig;

/**
 * Inventory that can provide itemstacks.
 * @author rubensworks
 */
public class InventoryItemStackProvider implements IItemStackProvider {

    protected Pair<Integer, ItemStack> getFirstItem(Container inventory, Direction side) {
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
    public boolean isValidForResults(Level world, BlockPos pos, Direction side) {
        IItemHandler itemHandler = BlockEntityHelpers.getCapability(world, pos, side, ForgeCapabilities.ITEM_HANDLER).orElse(null);
        Container inventory = BlockEntityHelpers.get(world, pos, Container.class).orElse(null);
        return itemHandler != null || inventory != null;
    }

    @Override
    public boolean hasItemStack(Level world, BlockPos pos, Direction side) {
        Container inventory = BlockEntityHelpers.get(world, pos, Container.class).orElse(null);
        IItemHandler itemHandler = BlockEntityHelpers.getCapability(world, pos, side, ForgeCapabilities.ITEM_HANDLER).orElse(null);
        return itemHandler != null || inventory != null;
    }

    @Override
    public ItemStack getItemStack(Level world, BlockPos pos, Direction side) {
        IItemHandler itemHandler = BlockEntityHelpers.getCapability(world, pos, side, ForgeCapabilities.ITEM_HANDLER).orElse(null);
        Container inventory = BlockEntityHelpers.get(world, pos, Container.class).orElse(null);
        Pair<Integer, ItemStack> result = itemHandler != null ? getFirstItem(itemHandler, side) : getFirstItem(inventory, side);
        if (result != null) {
            return result.getRight();
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean reduceItemStack(Level world, BlockPos pos, Direction side, boolean simulate) {
        IItemHandler itemHandler = BlockEntityHelpers.getCapability(world, pos, side, ForgeCapabilities.ITEM_HANDLER).orElse(null);
        if(itemHandler != null) {
            boolean extracted = false;
            for(int slot = 0; slot < itemHandler.getSlots(); slot++) {
                if(!itemHandler.extractItem(slot, 1, simulate).isEmpty()) {
                    extracted = true;
                    break;
                }
            }
            return extracted;
        } else {
            Container inventory = BlockEntityHelpers.get(world, pos, Container.class).orElse(null);
            Pair<Integer, ItemStack> result = getFirstItem(inventory, side);
            ItemStack newItemStack = result.getRight().copy();
            newItemStack.shrink(1);
            if (newItemStack.getCount() <= 0) {
                newItemStack = ItemStack.EMPTY;
            }
            if(!simulate) {
                inventory.setItem(result.getLeft(), newItemStack);
            }
            return true;
        }
    }

    @Override
    public boolean addItemStack(Level world, BlockPos pos, Direction side, ItemStack itemStack, boolean simulate) {
        IItemHandler itemHandler = BlockEntityHelpers.getCapability(world, pos, side, ForgeCapabilities.ITEM_HANDLER).orElse(null);
        if(itemHandler != null) {
            for(int slot = 0; slot < itemHandler.getSlots(); slot++) {
                if(itemHandler.insertItem(slot, itemStack, simulate).isEmpty()) {
                    return true;
                }
            }
        } else {
            Container inventory = BlockEntityHelpers.get(world, pos, Container.class).orElse(null);
            for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
                if (InventoryHelpers.addToSlot(inventory, slot, itemStack, simulate)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean setItemStack(Level world, BlockPos pos, Direction side, ItemStack itemStack, boolean simulate) {
        IItemHandler itemHandler = BlockEntityHelpers.getCapability(world, pos, side, ForgeCapabilities.ITEM_HANDLER).orElse(null);
        if(itemHandler != null) {
            for(int slot = 0; slot < itemHandler.getSlots(); slot++) {
                if(itemHandler.insertItem(slot, itemStack, simulate).isEmpty()) {
                    return true;
                }
            }
        } else {
            Container inventory = BlockEntityHelpers.get(world, pos, Container.class).orElse(null);
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
