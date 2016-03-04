package org.cyclops.structuredcrafting.craft.provider;

import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * Types of providers for itemstacks
 * @author rubensworks
 */
public interface IItemStackProvider {

    /**
     * Check if the given target is valid for output results for this provider type.
     * @param world The world.
     * @param pos The position.
     * @param side The side.
     * @return If valid.
     */
    public boolean isValidForResults(World world, BlockPos pos, EnumFacing side);

    /**
     * If the given provider has an itemstack at the given position.
     * @param world The world.
     * @param pos The position.
     * @param side The side.
     * @return If it has an itemstack.
     */
    public boolean hasItemStack(World world, BlockPos pos, EnumFacing side);

    /**
     * The itemstack at the given position.
     * @param world The world.
     * @param pos The position.
     * @param side The side.
     * @return The itemstack at the given position.
     */
    public ItemStack getItemStack(World world, BlockPos pos, EnumFacing side);

    /**
     * Consumes one amount of the itemstack, will only work if such a stack existed.
     * @param world The world.
     * @param pos The position.
     * @param side The side.
     * @param simulate If the operation should be simulated.
     */
    public void reduceItemStack(World world, BlockPos pos, EnumFacing side, boolean simulate);

    /**
     * Adds an itemstack.
     * @param world The world.
     * @param pos The position.
     * @param side The side.
     * @param itemStack The itemstack to set.
     * @param simulate If the operation should be simulated.
     * @return If the insertion succeeded.
     */
    public boolean addItemStack(World world, BlockPos pos, EnumFacing side, ItemStack itemStack, boolean simulate);

    /**
     * Set the itemstack.
     * @param world The world.
     * @param pos The position.
     * @param side The side.
     * @param itemStack The itemstack to set.
     * @param simulate If the operation should be simulated.
     * @return If the insertion succeeded.
     */
    public boolean setItemStack(World world, BlockPos pos, EnumFacing side, ItemStack itemStack, boolean simulate);

}
