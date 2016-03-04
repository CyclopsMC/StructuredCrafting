package org.cyclops.structuredcrafting.tileentity;

import lombok.experimental.Delegate;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;
import org.cyclops.structuredcrafting.craft.WorldCraftingMatrix;

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
        tickOffset = (int) (Math.random() * (float) SPEED);
    }

    public WorldCraftingMatrix getMatrix() {
        if(matrix == null) {
            matrix = WorldCraftingMatrix.deriveMatrix(worldObj, pos);
        }
        return matrix;
    }

    @Override
    protected void updateTileEntity() {
        tickOffset = (tickOffset + 1) % SPEED;
        if(!worldObj.isRemote && tickOffset == 0) {
            if(worldObj.isBlockPowered(getPos())) {
                getMatrix().craft(false);
            } else {
                matrix = null;
            }
        }
    }

}
