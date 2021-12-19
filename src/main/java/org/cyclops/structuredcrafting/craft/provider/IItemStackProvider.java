package org.cyclops.structuredcrafting.craft.provider;

import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

/**
 * Types of providers for itemstacks
 * @author rubensworks
 */
public interface IItemStackProvider {

    /**
     * @return If this provider can provide item inputs.
     */
    public boolean canProvideInput();

    /**
     * @return If this provider can handle item outputs.
     */
    public boolean canHandleOutput();

    /**
     * Check if the given target is valid for output results for this provider type.
     * @param world The world.
     * @param pos The position.
     * @param side The side.
     * @return If valid.
     */
    public boolean isValidForResults(Level world, BlockPos pos, Direction side);

    /**
     * If the given provider has an itemstack at the given position.
     * @param world The world.
     * @param pos The position.
     * @param side The side.
     * @return If it has an itemstack.
     */
    public boolean hasItemStack(Level world, BlockPos pos, Direction side);

    /**
     * The itemstack at the given position.
     * @param world The world.
     * @param pos The position.
     * @param side The side.
     * @return The itemstack at the given position.
     */
    public ItemStack getItemStack(Level world, BlockPos pos, Direction side);

    /**
     * Consumes one amount of the itemstack, will only work if such a stack existed.
     * @param world The world.
     * @param pos The position.
     * @param side The side.
     * @param simulate If the operation should be simulated.
     */
    public void reduceItemStack(Level world, BlockPos pos, Direction side, boolean simulate);

    /**
     * Adds an itemstack.
     * @param world The world.
     * @param pos The position.
     * @param side The side.
     * @param itemStack The itemstack to set.
     * @param simulate If the operation should be simulated.
     * @return If the insertion succeeded.
     */
    public boolean addItemStack(Level world, BlockPos pos, Direction side, ItemStack itemStack, boolean simulate);

    /**
     * Set the itemstack.
     * @param world The world.
     * @param pos The position.
     * @param side The side.
     * @param itemStack The itemstack to set.
     * @param simulate If the operation should be simulated.
     * @return If the insertion succeeded.
     */
    public boolean setItemStack(Level world, BlockPos pos, Direction side, ItemStack itemStack, boolean simulate);

}
