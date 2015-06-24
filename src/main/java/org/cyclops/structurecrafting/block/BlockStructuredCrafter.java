package org.cyclops.structurecrafting.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlock;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.structurecrafting.craft.WorldCraftingMatrix;

/**
 * This block will detect neighbour block updates and will try to craft a new block/item from them.
 * @author rubensworks
 */
public class BlockStructuredCrafter extends ConfigurableBlock {

    private static BlockStructuredCrafter _instance = null;

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static BlockStructuredCrafter getInstance() {
        return _instance;
    }

    /**
     * Make a new block instance.
     * @param eConfig Config for this block.
     */
    public BlockStructuredCrafter(ExtendedConfig eConfig) {
        super(eConfig, Material.iron);

        setHardness(3.0F);
        setStepSound(soundTypeMetal);
    }

    @Override
    public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block neighborBlock) {
        if(!world.isBlockPowered(pos)) {
            WorldCraftingMatrix[] matrices = WorldCraftingMatrix.deriveMatrices(world, pos);
            for (WorldCraftingMatrix matrix : matrices) {
                if (matrix.craft()) break;
            }
        }
    }

}
