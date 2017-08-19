package org.cyclops.structuredcrafting.craft;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.structuredcrafting.StructuredCrafting;
import org.cyclops.structuredcrafting.block.BlockStructuredCrafter;
import org.cyclops.structuredcrafting.craft.provider.IItemStackProvider;
import org.cyclops.structuredcrafting.craft.provider.IItemStackProviderRegistry;

import java.util.List;
import java.util.Map;

/**
 * A crafting matrix represented with blockstates.
 * @author rubensworks
 */
public class WorldCraftingMatrix {

    private final World world;
    private final BlockPos centerPos;
    private final EnumFacing.Axis axis;
    private final BlockPos targetPos;
    private final EnumFacing targetSide;
    private final EnumFacing inputSide;

    public WorldCraftingMatrix(World world, BlockPos centerPos, EnumFacing.Axis axis, BlockPos targetPos, EnumFacing inputSide) {
        this.world = world;
        this.centerPos = centerPos;
        this.axis = axis;
        this.targetPos = targetPos;
        this.targetSide = inputSide.getOpposite();
        this.inputSide = inputSide;
    }

    protected BlockPos addInAxis(BlockPos pos, EnumFacing.Axis axis, int i, int j) {
        if(axis == EnumFacing.Axis.X) {
            return pos.add(0, j, i);
        } else if(axis == EnumFacing.Axis.Y) {
            return pos.add(i, 0, j);
        } else if(axis == EnumFacing.Axis.Z) {
            return pos.add(i, j, 0);
        }
        return null;
    }

    protected List<IItemStackProvider> getItemStackProviders() {
        return StructuredCrafting._instance.getRegistryManager().
                getRegistry(IItemStackProviderRegistry.class).getProviders();
    }

    protected Map<ItemStack, IItemStackProvider> determineItemStackProviderForInput(World world, BlockPos pos, EnumFacing side) {
        Map<ItemStack, IItemStackProvider> providers = Maps.newHashMap();
        for(IItemStackProvider provider : getItemStackProviders()) {
            if(provider.hasItemStack(world, pos, side)) {
                providers.put(provider.getItemStack(world, pos, side), provider);
            }
        }
        return providers;
    }

    protected boolean addItemStackForOutput(World world, BlockPos pos, EnumFacing side, List<IItemStackProvider> outputProviders, ItemStack itemStack, boolean simulate) {
        for(IItemStackProvider provider : outputProviders) {
            if(provider.addItemStack(world, pos, side, itemStack, simulate)) {
                return true;
            }
        }
        return false;
    }

    public boolean craft(boolean simulate) {
        // Check if at least one of the providers can write to the output target.
        List<IItemStackProvider> outputProviders = Lists.newLinkedList();
        for(IItemStackProvider provider : getItemStackProviders()) {
            if(provider.isValidForResults(world, targetPos, targetSide)) {
                outputProviders.add(provider);
            }
        }
        if(outputProviders.isEmpty()) {
            return false;
        }

        CraftingPossibility chosenPossibility = null;
        ItemStack itemStack = null;
        for(int k = 0; k < 3; k++) {
            // Set crafting grid
            if(chosenPossibility == null) {
                List<CraftingPossibility> possibilities = Lists.newArrayList(new CraftingPossibility());
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        BlockPos pos = addInAxis(centerPos, axis, i, j);
                        Map<ItemStack, IItemStackProvider> results = determineItemStackProviderForInput(world, pos, inputSide);
                        boolean processedSingleResult = false;
                        List<CraftingPossibility> possibilitiesAtStart = Lists.newArrayList(possibilities);
                        for (Map.Entry<ItemStack, IItemStackProvider> result : results.entrySet()) {
                            ItemStack itemStackInput = result.getKey();
                            if (!itemStackInput.isEmpty()) {
                                itemStackInput = itemStackInput.copy();
                                itemStackInput.setCount(1);
                            }

                            if (!processedSingleResult) {
                                // The first result we process, just add position to all existing possibilities
                                for (CraftingPossibility possibility : possibilities) {
                                    possibility.setPosition(i, j, k, pos, result.getValue(), result.getKey());
                                }
                                processedSingleResult = true;
                            } else {
                                // We have more than one possibility!
                                // Duplicate all existing possibilities.
                                for (CraftingPossibility possibility : possibilitiesAtStart) {
                                    possibility = possibility.clone();
                                    possibility.setPosition(i, j, k, pos, result.getValue(), result.getKey());
                                    possibilities.add(possibility);
                                }
                            }
                        }

                        // No valid result was found, add an empty slot to all possibilities
                        if (!processedSingleResult) {
                            for (CraftingPossibility possibility : possibilities) {
                                possibility.setPosition(i, j, k, pos, null, ItemStack.EMPTY);
                            }
                        }
                    }
                }
                for (CraftingPossibility possibility : possibilities) {
                    if (!(itemStack = possibility.getOutput(world)).isEmpty()) {
                        chosenPossibility = possibility;
                        break;
                    }
                }
            }
        }

        // Determine output
        if(chosenPossibility != null && !itemStack.isEmpty()
                && addItemStackForOutput(world, targetPos, targetSide, outputProviders, itemStack, simulate)) {
            chosenPossibility.handleRemainingItems(world, inputSide, simulate);
            return true;
        }
        return false;
    }

    public static WorldCraftingMatrix deriveMatrix(World world, BlockPos centerPos) {
        EnumFacing side = (world.getBlockState(centerPos).getValue(BlockStructuredCrafter.FACING)).getOpposite();
        return new WorldCraftingMatrix(world, centerPos.offset(side), side.getAxis(),
                centerPos.offset(side.getOpposite()), side.getOpposite());
    }

    public static class CraftingPossibility {
        private final WorldInventoryCrafting inventoryCrafting = new WorldInventoryCrafting();
        private final BlockPos[] positions = new BlockPos[9];
        private final IItemStackProvider[] providers = new IItemStackProvider[9];

        public CraftingPossibility() {

        }

        public void setPosition(int i, int j, int rotation, BlockPos pos, IItemStackProvider itemStackProvider, ItemStack itemStack) {
            int arrayIndex = (j + 1) * 3 + (i + 1);
            // This makes sure we can also accept recipes which are rotated, mirroring is already supported by Vanilla.
            if (rotation == 0) {
                inventoryCrafting.setItemStack(i + 1, j + 1, itemStack);
            } else if(rotation == 1) {
                inventoryCrafting.setItemStack(j + 1, i + 1, itemStack);
            } else if(rotation == 2) {
                inventoryCrafting.setItemStack(i + 1, 2 - (j + 1), itemStack);
            }
            positions[arrayIndex] = pos;
            providers[arrayIndex] = itemStackProvider;
        }

        public ItemStack getOutput(World world) {
            return CraftingManager.getInstance().findMatchingRecipe(inventoryCrafting, world);
        }

        /**
         * Handle remaining container items: place blocks and drop items
         * @param world The world.
         * @param inputSide The crafting side.
         * @param simulate If the crafting should be simulated.
         */
        public void handleRemainingItems(World world, EnumFacing inputSide, boolean simulate) {
            NonNullList<ItemStack> remainingStacks = CraftingManager.getInstance().getRemainingItems(inventoryCrafting, world);
            for(int i = 0; i < remainingStacks.size(); i++) {
                ItemStack remainingStack = remainingStacks.get(i);
                if(providers[i] != null) {
                    providers[i].reduceItemStack(world, positions[i], inputSide, simulate);
                    if (!remainingStack.isEmpty() && remainingStack.getCount() > 0) {
                        providers[i].addItemStack(world, positions[i], inputSide, remainingStack, simulate);
                    }
                }
            }
        }

        public CraftingPossibility clone() {
            CraftingPossibility craftingPossibility = new CraftingPossibility();
            for (int i = 0; i < this.inventoryCrafting.getSizeInventory(); i++) {
                craftingPossibility.inventoryCrafting.setInventorySlotContents(i, this.inventoryCrafting.getStackInSlot(i));
            }
            System.arraycopy(this.positions, 0, craftingPossibility.positions, 0, this.positions.length);
            System.arraycopy(this.providers, 0, craftingPossibility.providers, 0, this.providers.length);
            return craftingPossibility;
        }
    }

}
