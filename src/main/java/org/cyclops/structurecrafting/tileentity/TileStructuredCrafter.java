package org.cyclops.structurecrafting.tileentity;

import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;
import org.cyclops.structurecrafting.craft.WorldCraftingMatrix;

/**
 * A ticking tile entity for the structured crafter.
 * @author rubensworks
 */
public class TileStructuredCrafter extends CyclopsTileEntity implements CyclopsTileEntity.ITickingTile {

    private static final int SPEED = 20;

    private int tickOffset;

    public TileStructuredCrafter() {
        tickOffset = (int) (Math.random() * (float) SPEED);
    }

    @Override
    protected void updateTileEntity() {
        tickOffset = (tickOffset + 1) % SPEED;
        if(!worldObj.isRemote && tickOffset == 0 && worldObj.isBlockPowered(getPos())) {
            WorldCraftingMatrix matrix = WorldCraftingMatrix.deriveMatrix(worldObj, pos);
            matrix.craft();
        }
    }

}
