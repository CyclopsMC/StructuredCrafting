package org.cyclops.structuredcrafting.craft.provider;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.cyclopscore.helper.IModHelpersNeoForge;
import org.cyclops.structuredcrafting.block.BlockStructuredCrafterConfig;

/**
 * Inventory that can provide itemstacks.
 * @author rubensworks
 */
public class InventoryItemStackProviderNeoForge implements IItemStackProvider {

    protected Pair<Integer, ItemStack> getFirstItem(Container inventory, Direction side) {
        for(int slot = 0; slot < inventory.getContainerSize(); slot++) {
            ItemStack itemStack = inventory.getItem(slot);
            if(!itemStack.isEmpty()) {
                return Pair.of(slot, itemStack);
            }
        }
        return null;
    }

    protected Pair<Integer, ItemStack> getFirstItem(ResourceHandler<ItemResource> itemHandler, Direction side) {
        for(int slot = 0; slot < itemHandler.size(); slot++) {
            try (var tx = Transaction.openRoot()) {
                ItemResource resource = itemHandler.getResource(slot);
                if (!resource.isEmpty()) {
                    int extracted = itemHandler.extract(slot, resource, 1, tx);
                    if (extracted > 0) {
                        return Pair.of(slot, resource.toStack(extracted));
                    }
                }
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
        ResourceHandler<ItemResource> itemHandler = IModHelpersNeoForge.get().getCapabilityHelpers().getCapability(world, pos, side, Capabilities.Item.BLOCK).orElse(null);
        Container inventory = IModHelpers.get().getBlockEntityHelpers().get(world, pos, Container.class).orElse(null);
        return itemHandler != null || inventory != null;
    }

    @Override
    public boolean hasItemStack(Level world, BlockPos pos, Direction side) {
        Container inventory = IModHelpers.get().getBlockEntityHelpers().get(world, pos, Container.class).orElse(null);
        ResourceHandler<ItemResource> itemHandler = IModHelpersNeoForge.get().getCapabilityHelpers().getCapability(world, pos, side, Capabilities.Item.BLOCK).orElse(null);
        return itemHandler != null || inventory != null;
    }

    @Override
    public ItemStack getItemStack(Level world, BlockPos pos, Direction side) {
        ResourceHandler<ItemResource> itemHandler = IModHelpersNeoForge.get().getCapabilityHelpers().getCapability(world, pos, side, Capabilities.Item.BLOCK).orElse(null);
        Container inventory = IModHelpers.get().getBlockEntityHelpers().get(world, pos, Container.class).orElse(null);
        Pair<Integer, ItemStack> result = itemHandler != null ? getFirstItem(itemHandler, side) : getFirstItem(inventory, side);
        if (result != null) {
            return result.getRight();
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean reduceItemStack(Level world, BlockPos pos, Direction side, boolean simulate) {
        ResourceHandler<ItemResource> itemHandler = IModHelpersNeoForge.get().getCapabilityHelpers().getCapability(world, pos, side, Capabilities.Item.BLOCK).orElse(null);
        if (itemHandler != null) {
            boolean extracted = false;
            for(int slot = 0; slot < itemHandler.size(); slot++) {
                ItemResource resource = itemHandler.getResource(slot);
                if (!resource.isEmpty()) {
                    try (var tx = Transaction.openRoot()) {
                        int extractedCount = itemHandler.extract(slot, resource, 1, tx);
                        if (!simulate) {
                            tx.commit();
                        }
                        if (extractedCount > 0) {
                            extracted = true;
                            break;
                        }
                    }
                }
            }
            return extracted;
        } else {
            Container inventory = IModHelpers.get().getBlockEntityHelpers().get(world, pos, Container.class).orElse(null);
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
        ResourceHandler<ItemResource> itemHandler = IModHelpersNeoForge.get().getCapabilityHelpers().getCapability(world, pos, side, Capabilities.Item.BLOCK).orElse(null);
        if (itemHandler != null) {
            for (int slot = 0; slot < itemHandler.size(); slot++) {
                try (var tx = Transaction.openRoot()) {
                    int inserted = itemHandler.insert(slot, ItemResource.of(itemStack), itemStack.getCount(), tx);
                    if (!simulate) {
                        tx.commit();
                    }
                    if (inserted > 0) {
                        return true;
                    }
                }
            }
        } else {
            Container inventory = IModHelpers.get().getBlockEntityHelpers().get(world, pos, Container.class).orElse(null);
            for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
                if (IModHelpers.get().getInventoryHelpers().addToSlot(inventory, slot, itemStack, simulate)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean setItemStack(Level world, BlockPos pos, Direction side, ItemStack itemStack, boolean simulate) {
        ResourceHandler<ItemResource> itemHandler = IModHelpersNeoForge.get().getCapabilityHelpers().getCapability(world, pos, side, Capabilities.Item.BLOCK).orElse(null);
        if (itemHandler != null) {
            for (int slot = 0; slot < itemHandler.size(); slot++) {
                try (var tx = Transaction.openRoot()) {
                    int inserted = itemHandler.insert(slot, ItemResource.of(itemStack), itemStack.getCount(), tx);
                    if (!simulate) {
                        tx.commit();
                    }
                    if (inserted > 0) {
                        return true;
                    }
                }
            }
        } else {
            Container inventory = IModHelpers.get().getBlockEntityHelpers().get(world, pos, Container.class).orElse(null);
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
