package org.cyclops.structuredcrafting.tileentity;

import lombok.experimental.Delegate;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;
import org.cyclops.structuredcrafting.RegistryEntries;
import org.cyclops.structuredcrafting.craft.WorldCraftingMatrix;

import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity.ITickingTile;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity.TickingTileComponent;

/**
 * A ticking tile entity for the structured crafter.
 * @author rubensworks
 */
public class TileStructuredCrafter extends CyclopsTileEntity implements CyclopsTileEntity.ITickingTile {

    private static final int SPEED = 20;

    protected WorldCraftingMatrix matrix = null;

    @Delegate
    private final ITickingTile tickingTileComponent = new TickingTileComponent(this);

    private int tickOffset;

    public TileStructuredCrafter() {
        super(RegistryEntries.TILE_STRUCTURED_CRAFTER);
        tickOffset = (int) (Math.random() * (float) SPEED);
    }

    public WorldCraftingMatrix getMatrix() {
        if(matrix == null) {
            matrix = WorldCraftingMatrix.deriveMatrix(level, worldPosition);
        }
        return matrix;
    }

    @Override
    protected void updateTileEntity() {
        tickOffset = (tickOffset + 1) % SPEED;
        if(!level.isClientSide && tickOffset == 0) {
            if(level.hasNeighborSignal(getBlockPos())) {
                getMatrix().craft(false);
            } else {
                matrix = null;
            }
        }
    }

}
