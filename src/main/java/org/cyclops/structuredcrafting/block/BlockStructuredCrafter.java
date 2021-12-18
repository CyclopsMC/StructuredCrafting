package org.cyclops.structuredcrafting.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.block.BlockTile;
import org.cyclops.structuredcrafting.tileentity.TileStructuredCrafter;

import javax.annotation.Nullable;

/**
 * This block will detect neighbour block updates and will try to craft a new block/item from them.
 * @author rubensworks
 */
public class BlockStructuredCrafter extends BlockTile {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public BlockStructuredCrafter(Block.Properties properties) {
        super(properties, TileStructuredCrafter::new);

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.DOWN));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getClickedFace().getOpposite());
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand,
                                             BlockRayTraceResult hit) {
        ItemStack heldItem = player.getItemInHand(hand);
        if(player != null && !heldItem.isEmpty() && heldItem.getItem() == Items.STICK) {
            worldIn.setBlockAndUpdate(pos, state.setValue(FACING, hit.getDirection().getOpposite()));
            return ActionResultType.SUCCESS;
        }
        return super.use(state, worldIn, pos, player, hand, hit);
    }

}
