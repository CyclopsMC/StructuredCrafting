package org.cyclops.structuredcrafting.craft;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.ToString;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
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

    private final Level level;
    private final BlockPos centerPos;
    private final Direction.Axis axis;
    private final BlockPos targetPos;
    private final Direction targetSide;
    private final Direction inputSide;

    public WorldCraftingMatrix(Level level, BlockPos centerPos, Direction.Axis axis, BlockPos targetPos, Direction inputSide) {
        this.level = level;
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

    protected Map<ItemStack, IItemStackProvider> determineItemStackProviderForInput(Level level, BlockPos pos, Direction side) {
        Map<ItemStack, IItemStackProvider> providers = Maps.newHashMap();
        for(IItemStackProvider provider : getItemStackProviders()) {
            if(provider.canProvideInput() && provider.hasItemStack(level, pos, side)) {
                providers.put(provider.getItemStack(level, pos, side), provider);
            }
        }
        return providers;
    }

    protected boolean addItemStackForOutput(Level level, BlockPos pos, Direction side, List<IItemStackProvider> outputProviders, ItemStack itemStack, boolean simulate) {
        for(IItemStackProvider provider : outputProviders) {
            if(provider.canHandleOutput() && provider.addItemStack(level, pos, side, itemStack, simulate)) {
                return true;
            }
        }
        return false;
    }

    public boolean craft(boolean simulate) {
        // Check if at least one of the providers can write to the output target.
        List<IItemStackProvider> outputProviders = Lists.newLinkedList();
        for(IItemStackProvider provider : getItemStackProviders()) {
            if(provider.isValidForResults(level, targetPos, targetSide)) {
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
                        Map<ItemStack, IItemStackProvider> results = determineItemStackProviderForInput(level, pos, inputSide);
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
                    if (!(itemStack = possibility.getOutput(level)).isEmpty()) {
                        chosenPossibility = possibility;
                        break;
                    }
                }
            }
        }

        // Determine output
        if(chosenPossibility != null && !itemStack.isEmpty()
                && addItemStackForOutput(level, targetPos, targetSide, outputProviders, itemStack, true)) {
            if (chosenPossibility.handleRemainingItems(level, inputSide, simulate)) {
                if (!simulate) {
                    addItemStackForOutput(level, targetPos, targetSide, outputProviders, itemStack, simulate);
                }
                return true;
            }
        }
        return false;
    }

    public static WorldCraftingMatrix deriveMatrix(Level level, BlockPos centerPos) {
        Direction side = (level.getBlockState(centerPos).getValue(BlockStructuredCrafter.FACING)).getOpposite();
        return new WorldCraftingMatrix(level, centerPos.relative(side), side.getAxis(),
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

        protected Recipe getRecipe(Level level) {
            return CraftingHelpers.findRecipeCached(RecipeType.CRAFTING, inventoryCrafting, level, true).orElse(null);
        }

        public ItemStack getOutput(Level level) {
            Recipe recipe = getRecipe(level);
            if (recipe != null) {
                return recipe.assemble(inventoryCrafting, level.registryAccess());
            }
            return ItemStack.EMPTY;
        }

        /**
         * Handle remaining container items: place blocks and drop items
         * @param level The level.
         * @param inputSide The crafting side.
         * @param simulate If the crafting should be simulated.
         */
        public boolean handleRemainingItems(Level level, Direction inputSide, boolean simulate) {
            Recipe recipe = getRecipe(level);
            NonNullList<ItemStack> remainingStacks = recipe.getRemainingItems(inventoryCrafting);
            for(int i = 0; i < remainingStacks.size(); i++) {
                ItemStack originalStack = inventoryCrafting.getItem(i);
                ItemStack remainingStack = remainingStacks.get(i);
                if(originalStack != null && !originalStack.isEmpty()) {
                    if (providers[i] != null) {
                        // Consume one item from input
                        boolean success = providers[i].reduceItemStack(level, positions[i], inputSide, simulate);

                        // If consumption failed, consider the whole crafting job failed
                        if (!success) {
                            // Restore all previous slots if not simulating
                            if (!simulate) {
                                for(int j = 0; j < i; j++) {
                                    ItemStack stackToRestore = inventoryCrafting.getItem(j);
                                    if(stackToRestore != null && !stackToRestore.isEmpty() && providers[j] != null) {
                                        providers[j].addItemStack(level, positions[j], inputSide, stackToRestore, false);
                                    }
                                }
                            }

                            return false;
                        }

                        // Add a possibly remaining stack to the slot
                        if (!remainingStack.isEmpty() && remainingStack.getCount() > 0) {
                            providers[i].addItemStack(level, positions[i], inputSide, remainingStack, simulate);
                        }
                    } else {
                        StructuredCrafting.clog(org.apache.logging.log4j.Level.WARN, "The structured crafting provider for position "
                                + i + " did not exist for stack " + originalStack);
                    }
                }
            }
            return true;
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
