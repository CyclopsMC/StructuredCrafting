package org.cyclops.structuredcrafting.craft;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.structuredcrafting.StructuredCrafting;
import org.cyclops.structuredcrafting.block.BlockStructuredCrafter;
import org.cyclops.structuredcrafting.craft.provider.IItemStackProvider;
import org.cyclops.structuredcrafting.craft.provider.IItemStackProviderRegistry;

import java.util.List;

/**
 * A crafting matrix represented with blockstates.
 * @author rubensworks
 */
public class WorldCraftingMatrix {

    private static final WorldInventoryCrafting INVENTORY_CRAFTING = new WorldInventoryCrafting();

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

    protected Pair<ItemStack, IItemStackProvider> determineItemStackProviderForInput(World world, BlockPos pos, EnumFacing side) {
        for(IItemStackProvider provider : getItemStackProviders()) {
            if(provider.hasItemStack(world, pos, side)) {
                return Pair.of(provider.getItemStack(world, pos, side), provider);
            }
        }
        return null;
    }

    protected boolean addItemStackForOutput(World world, BlockPos pos, EnumFacing side, List<IItemStackProvider> outputProviders, ItemStack itemStack) {
        for(IItemStackProvider provider : outputProviders) {
            if(provider.addItemStack(world, pos, side, itemStack)) {
                return true;
            }
        }
        return false;
    }

    public boolean craft() {
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

        BlockPos[] positions = new BlockPos[9];
        IItemStackProvider[] providers = new IItemStackProvider[9];
        ItemStack itemStack = null;
        for(int k = 0; k < 2; k++) {
            // Set crafting grid
            if(itemStack == null) {
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        int arrayIndex = (j + 1) * 3 + (i + 1);
                        BlockPos pos = addInAxis(centerPos, axis, i, j);
                        Pair<ItemStack, IItemStackProvider> result = determineItemStackProviderForInput(world, pos, inputSide);
                        ItemStack itemStackInput = result != null ? result.getLeft() : null;
                        if (itemStackInput != null) {
                            itemStackInput = itemStackInput.copy();
                            itemStackInput.stackSize = 1;
                        }
                        // This makes sure we can also accept recipes which are rotated, mirroring is already supported by Vanilla.
                        if (k == 0) {
                            INVENTORY_CRAFTING.setItemStack(i + 1, j + 1, itemStackInput);
                        } else {
                            INVENTORY_CRAFTING.setItemStack(j + 1, i + 1, itemStackInput);
                        }
                        positions[arrayIndex] = pos;
                        providers[arrayIndex] = result != null ? result.getRight() : null;
                    }
                }
                itemStack = CraftingManager.getInstance().findMatchingRecipe(INVENTORY_CRAFTING, world);
            }
        }

        // Determine output
        if(itemStack != null && addItemStackForOutput(world, targetPos, targetSide, outputProviders, itemStack)) {
            // Handle remaining container items: place blocks and drop items
            ItemStack[] remainingStacks = CraftingManager.getInstance().func_180303_b(INVENTORY_CRAFTING, world);
            for(int i = 0; i < remainingStacks.length; i++) {
                ItemStack remainingStack = remainingStacks[i];
                if(providers[i] != null) {
                    providers[i].reduceItemStack(world, positions[i], inputSide);
                    if (remainingStack != null && remainingStack.stackSize > 0) {
                        providers[i].addItemStack(world, positions[i], inputSide, remainingStack);
                    }
                }
            }
            return true;
        }
        return false;
    }

    public static WorldCraftingMatrix deriveMatrix(World world, BlockPos centerPos) {
        EnumFacing side = ((EnumFacing) world.getBlockState(centerPos).getValue(BlockStructuredCrafter.FACING)).getOpposite();
        return new WorldCraftingMatrix(world, centerPos.offset(side), side.getAxis(),
                centerPos.offset(side.getOpposite()), side.getOpposite());
    }

}
