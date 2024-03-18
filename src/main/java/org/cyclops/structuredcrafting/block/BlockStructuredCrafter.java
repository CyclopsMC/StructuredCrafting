package org.cyclops.structuredcrafting.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.cyclops.cyclopscore.block.BlockWithEntity;
import org.cyclops.structuredcrafting.RegistryEntries;
import org.cyclops.structuredcrafting.blockentity.BlockEntityStructuredCrafter;

import javax.annotation.Nullable;

/**
 * This block will detect neighbour block updates and will try to craft a new block/item from them.
 * @author rubensworks
 */
public class BlockStructuredCrafter extends BlockWithEntity {

    public static final MapCodec<BlockStructuredCrafter> CODEC = simpleCodec(BlockStructuredCrafter::new);

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public BlockStructuredCrafter(Block.Properties properties) {
        super(properties, BlockEntityStructuredCrafter::new);

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.DOWN));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, RegistryEntries.BLOCK_ENTITY_STRUCTURED_CRAFTER.get(), new BlockEntityStructuredCrafter.Ticker());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getClickedFace().getOpposite());
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand hand,
                                             BlockHitResult hit) {
        ItemStack heldItem = player.getItemInHand(hand);
        if(player != null && !heldItem.isEmpty() && heldItem.getItem() == Items.STICK) {
            worldIn.setBlockAndUpdate(pos, state.setValue(FACING, hit.getDirection().getOpposite()));
            return InteractionResult.SUCCESS;
        }
        return super.use(state, worldIn, pos, player, hand, hit);
    }

}
