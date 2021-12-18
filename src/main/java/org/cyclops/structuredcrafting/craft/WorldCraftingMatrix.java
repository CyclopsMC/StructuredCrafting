package org.cyclops.structuredcrafting.craft;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.ToString;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.helper.CraftingHelpers;
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
    private final Direction.Axis axis;
    private final BlockPos targetPos;
    private final Direction targetSide;
    private final Direction inputSide;

    public WorldCraftingMatrix(World world, BlockPos centerPos, Direction.Axis axis, BlockPos targetPos, Direction inputSide) {
        this.world = world;
        this.centerPos = centerPos;
        this.axis = axis;
        this.targetPos = targetPos;
        this.targetSide = inputSide.getOpposite();
        this.inputSide = inputSide;
    }

    protected BlockPos addInAxis(BlockPos pos, Direction.Axis axis, int i, int j) {
        if(axis == Direction.Axis.X) {
            return pos.offset(0, j, i);
        } else if(axis == Direction.Axis.Y) {
            return pos.offset(i, 0, j);
        } else if(axis == Direction.Axis.Z) {
            return pos.offset(i, j, 0);
        }
        return null;
    }

    protected List<IItemStackProvider> getItemStackProviders() {
        return StructuredCrafting._instance.getRegistryManager().
                getRegistry(IItemStackProviderRegistry.class).getProviders();
    }

    protected Map<ItemStack, IItemStackProvider> determineItemStackProviderForInput(World world, BlockPos pos, Direction side) {
        Map<ItemStack, IItemStackProvider> providers = Maps.newHashMap();
        for(IItemStackProvider provider : getItemStackProviders()) {
            if(provider.canProvideInput() && provider.hasItemStack(world, pos, side)) {
                providers.put(provider.getItemStack(world, pos, side), provider);
            }
        }
        return providers;
    }

    protected boolean addItemStackForOutput(World world, BlockPos pos, Direction side, List<IItemStackProvider> outputProviders, ItemStack itemStack, boolean simulate) {
        for(IItemStackProvider provider : outputProviders) {
            if(provider.canHandleOutput() && provider.addItemStack(world, pos, side, itemStack, simulate)) {
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
        Direction side = (world.getBlockState(centerPos).getValue(BlockStructuredCrafter.FACING)).getOpposite();
        return new WorldCraftingMatrix(world, centerPos.relative(side), side.getAxis(),
                centerPos.relative(side.getOpposite()), side.getOpposite());
    }

    @ToString
    public static class CraftingPossibility {
        private final WorldInventoryCrafting inventoryCrafting = new WorldInventoryCrafting();
        private final BlockPos[] positions = new BlockPos[9];
        private final IItemStackProvider[] providers = new IItemStackProvider[9];

        public CraftingPossibility() {

        }

        public void setPosition(int i, int j, int rotation, BlockPos pos, IItemStackProvider itemStackProvider, ItemStack itemStack) {
            int row, col;
            // This makes sure we can also accept recipes which are rotated, mirroring is already supported by Vanilla.
            if (rotation == 0) {
                row = i + 1;
                col = j + 1;
            } else if(rotation == 1) {
                row = j + 1;
                col = i + 1;
            } else if(rotation == 2) {
                row = i + 1;
                col = 2 - (j + 1);
            } else {
                throw new IllegalStateException("Invalid rotation: " + rotation);
            }
            inventoryCrafting.setItemStack(row, col, itemStack);
            int arrayIndex = col * 3 + row;
            positions[arrayIndex] = pos;
            providers[arrayIndex] = itemStackProvider;
        }

        protected IRecipe getRecipe(World world) {
            return CraftingHelpers.findRecipeCached(IRecipeType.CRAFTING, inventoryCrafting, world, true).orElse(null);
        }

        public ItemStack getOutput(World world) {
            IRecipe recipe = getRecipe(world);
            if (recipe != null) {
                return recipe.assemble(inventoryCrafting);
            }
            return ItemStack.EMPTY;
        }

        /**
         * Handle remaining container items: place blocks and drop items
         * @param world The world.
         * @param inputSide The crafting side.
         * @param simulate If the crafting should be simulated.
         */
        public void handleRemainingItems(World world, Direction inputSide, boolean simulate) {
            IRecipe recipe = getRecipe(world);
            NonNullList<ItemStack> remainingStacks = recipe.getRemainingItems(inventoryCrafting);
            for(int i = 0; i < remainingStacks.size(); i++) {
                ItemStack originalStack = inventoryCrafting.getItem(i);
                ItemStack remainingStack = remainingStacks.get(i);
                if(originalStack != null && !originalStack.isEmpty()) {
                    if (providers[i] != null) {
                        providers[i].reduceItemStack(world, positions[i], inputSide, simulate);
                        if (!remainingStack.isEmpty() && remainingStack.getCount() > 0) {
                            providers[i].addItemStack(world, positions[i], inputSide, remainingStack, simulate);
                        }
                    } else {
                        StructuredCrafting.clog(Level.WARN, "The structured crafting provider for position "
                                + i + " did not exist for stack " + originalStack);
                    }
                }
            }
        }

        public CraftingPossibility clone() {
            CraftingPossibility craftingPossibility = new CraftingPossibility();
            for (int i = 0; i < this.inventoryCrafting.getContainerSize(); i++) {
                craftingPossibility.inventoryCrafting.setItem(i, this.inventoryCrafting.getItem(i));
            }
            System.arraycopy(this.positions, 0, craftingPossibility.positions, 0, this.positions.length);
            System.arraycopy(this.providers, 0, craftingPossibility.providers, 0, this.providers.length);
            return craftingPossibility;
        }
    }

}
