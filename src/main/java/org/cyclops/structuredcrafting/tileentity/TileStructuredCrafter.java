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

    @Delegate
    private final ITickingTile tickingTileComponent = new TickingTileComponent(this);

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
