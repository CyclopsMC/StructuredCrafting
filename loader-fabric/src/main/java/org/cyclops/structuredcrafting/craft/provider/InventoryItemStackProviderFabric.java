package org.cyclops.structuredcrafting.craft.provider;

import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.cyclops.structuredcrafting.block.BlockStructuredCrafterConfig;

import java.util.Iterator;

/**
 * Inventory that can provide itemstacks.
 * @author rubensworks
 */
public class InventoryItemStackProviderFabric implements IItemStackProvider {

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
        return ItemStorage.SIDED.find(world, pos, side) != null;
    }

    @Override
    public boolean hasItemStack(Level world, BlockPos pos, Direction side) {
        return ItemStorage.SIDED.find(world, pos, side) != null;
    }

    @Override
    public ItemStack getItemStack(Level world, BlockPos pos, Direction side) {
        Storage<ItemVariant> storage = ItemStorage.SIDED.find(world, pos, side);
        if (storage != null) {
            Iterator<StorageView<ItemVariant>> it = storage.nonEmptyIterator();
            if (it.hasNext()) {
                return it.next().getResource().toStack();
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean reduceItemStack(Level world, BlockPos pos, Direction side, boolean simulate) {
        Storage<ItemVariant> storage = ItemStorage.SIDED.find(world, pos, side);
        if (storage != null) {
            Iterator<StorageView<ItemVariant>> it = storage.nonEmptyIterator();
            if (it.hasNext()) {
                ItemStack firstStack = it.next().getResource().toStack();
                try (Transaction tx = Transaction.openOuter()) {
                    long extracted = storage.extract(ItemVariant.of(firstStack), 1, tx);
                    if (simulate) {
                        tx.abort();
                    } else {
                        tx.commit();
                    }
                    return extracted > 0;
                }
            }
        }
        return false;
    }

    @Override
    public boolean addItemStack(Level world, BlockPos pos, Direction side, ItemStack itemStack, boolean simulate) {
        Storage<ItemVariant> storage = ItemStorage.SIDED.find(world, pos, side);
        if (storage != null) {
            try (Transaction tx = Transaction.openOuter()) {
                long inserted = storage.insert(ItemVariant.of(itemStack), 1, tx);
                if (simulate) {
                    tx.abort();
                } else {
                    tx.commit();
                }
                return inserted > 0;
            }
        }
        return false;
    }

    @Override
    public boolean setItemStack(Level world, BlockPos pos, Direction side, ItemStack itemStack, boolean simulate) {
        return addItemStack(world, pos, side, itemStack, simulate);
    }
}
