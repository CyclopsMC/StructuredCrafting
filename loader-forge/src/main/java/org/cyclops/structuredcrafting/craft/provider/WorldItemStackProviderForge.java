package org.cyclops.structuredcrafting.craft.provider;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import org.cyclops.cyclopscore.helper.IModHelpersForge;

/**
 * World that can provide an itemstack.
 * @author rubensworks
 */
public class WorldItemStackProviderForge extends WorldItemStackProviderBase {

    private final IModHelpersForge modHelpers;

    public WorldItemStackProviderForge(IModHelpersForge modHelpers) {
        this.modHelpers = modHelpers;
    }

    @Override
    protected boolean hasEmptyItemHandler(Level world, BlockPos pos, Direction side) {
        IItemHandler itemHandler = this.modHelpers.getCapabilityHelpers().getCapability(world, pos, side, ForgeCapabilities.ITEM_HANDLER).orElse(null);
        boolean emptyItemHandler = true;
        if (itemHandler != null) {
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                if (!itemHandler.extractItem(i, 1, true).isEmpty()) {
                    emptyItemHandler = false;
                    break;
                }
            }
        }
        return emptyItemHandler;
    }

    @Override
    public ItemStack getItemStack(Level world, BlockPos pos, Direction side) {
        BlockState blockState = world.getBlockState(pos);
        if(blockState != null && hasEmptyItemHandler(world, pos, side)) {
            return blockState.getBlock().getCloneItemStack(world, pos, blockState);
        }
        return ItemStack.EMPTY;
    }

}
