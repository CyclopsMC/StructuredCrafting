package org.cyclops.structurecrafting.craft;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

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

    public WorldCraftingMatrix(World world, BlockPos centerPos, EnumFacing.Axis axis, BlockPos targetPos) {
        this.world = world;
        this.centerPos = centerPos;
        this.axis = axis;
        this.targetPos = targetPos;
    }

    protected BlockPos addInAxis(BlockPos pos, EnumFacing.Axis axis, int i, int j) {
        if(axis == EnumFacing.Axis.X) {
            return pos.add(0, i, j);
        } else if(axis == EnumFacing.Axis.Y) {
            return pos.add(i, 0, j);
        } else if(axis == EnumFacing.Axis.Z) {
            return pos.add(i, j, 0);
        }
        return null;
    }

    public boolean craft() {
        if(!world.isAirBlock(targetPos)) {
            return false;
        }

        BlockPos[] positions = new BlockPos[9];

        // Set crafting grid
        for(int i = -1; i < 2; i++) {
            for(int j = -1; j < 2; j++) {
                BlockPos pos = addInAxis(centerPos, axis, i, j);
                IBlockState blockState = world.getBlockState(pos);

                ItemStack itemStack = null;
                if(blockState != null) {
                    Item item = blockState.getBlock().getItem(world, pos);
                    if(item != null) {
                        itemStack = new ItemStack(item, blockState.getBlock().getDamageValue(world, pos));
                    }
                }
                INVENTORY_CRAFTING.setItemStack(i + 1, j + 1, itemStack);
                positions[(j + 1) * 3 + (i + 1)] = pos;
            }
        }

        // Determine output
        ItemStack itemStack = CraftingManager.getInstance().findMatchingRecipe(INVENTORY_CRAFTING, world);
        if(itemStack != null && itemStack.getItem() instanceof ItemBlock) {
            // replace blocks depending on what the crafting results are.
            world.setBlockState(targetPos, ((ItemBlock) itemStack.getItem()).getBlock().getStateFromMeta(itemStack.getItemDamage()));

            // Handle remaining container items: place blocks and drop items
            for(int i = 0; i < INVENTORY_CRAFTING.getSizeInventory(); i++) {
                ItemStack remainingStack = INVENTORY_CRAFTING.getStackInSlot(i);
                if(remainingStack != null && remainingStack.stackSize > 0) {
                    if(remainingStack.getItem() instanceof ItemBlock) {
                        world.setBlockState(positions[i],((ItemBlock) remainingStack.getItem()).getBlock().
                                getStateFromMeta(remainingStack.getItemDamage()));
                    } else {
                        // TODO: drop as entity, temp
                    }
                } else {
                    world.setBlockToAir(positions[i]);
                }
            }
            return true;
        }

        return false;
    }

    public static WorldCraftingMatrix[] deriveMatrices(World world, BlockPos centerPos) {
        final WorldCraftingMatrix[] matrices = new WorldCraftingMatrix[6]; // Test for all faces of the center position.
        for(int i = 0; i < EnumFacing.values().length; i++) {
            EnumFacing side = EnumFacing.values()[i];
            matrices[i] = new WorldCraftingMatrix(world, centerPos.offset(side), side.getAxis(), centerPos.offset(side.getOpposite()));
        }
        return matrices;
    }

}
