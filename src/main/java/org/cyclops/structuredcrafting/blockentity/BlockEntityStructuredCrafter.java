package org.cyclops.structuredcrafting.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.cyclops.cyclopscore.blockentity.BlockEntityTickerDelayed;
import org.cyclops.cyclopscore.blockentity.CyclopsBlockEntity;
import org.cyclops.structuredcrafting.RegistryEntries;
import org.cyclops.structuredcrafting.craft.WorldCraftingMatrix;

/**
 * A ticking block entity for the structured crafter.
 * @author rubensworks
 */
public class BlockEntityStructuredCrafter extends CyclopsBlockEntity {

    private static final int SPEED = 20;

    protected WorldCraftingMatrix matrix = null;

    private int tickOffset;

    public BlockEntityStructuredCrafter(BlockPos blockPos, BlockState blockState) {
        super(RegistryEntries.BLOCK_ENTITY_STRUCTURED_CRAFTER.get(), blockPos, blockState);
        tickOffset = (int) (Math.random() * (float) SPEED);
    }

    public WorldCraftingMatrix getMatrix() {
        if(matrix == null) {
            matrix = WorldCraftingMatrix.deriveMatrix(level, worldPosition);
        }
        return matrix;
    }

    public void setMatrix(WorldCraftingMatrix matrix) {
        this.matrix = matrix;
    }

    public int getTickOffset() {
        return tickOffset;
    }

    public void setTickOffset(int tickOffset) {
        this.tickOffset = tickOffset;
    }

    public static class Ticker extends BlockEntityTickerDelayed<BlockEntityStructuredCrafter> {
        @Override
        protected void update(Level level, BlockPos pos, BlockState blockState, BlockEntityStructuredCrafter blockEntity) {
            super.update(level, pos, blockState, blockEntity);

            blockEntity.setTickOffset((blockEntity.getTickOffset() + 1) % SPEED);
            if(!level.isClientSide && blockEntity.getTickOffset() == 0) {
                if(level.hasNeighborSignal(pos)) {
                    blockEntity.getMatrix().craft(false);
                } else {
                    blockEntity.setMatrix(null);
                }
            }
        }
    }

}
