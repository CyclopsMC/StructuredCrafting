package org.cyclops.structuredcrafting.craft.provider;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.items.IItemHandler;
import org.cyclops.cyclopscore.helper.BlockEntityHelpers;
import org.cyclops.cyclopscore.helper.ItemStackHelpers;
import org.cyclops.structuredcrafting.block.BlockStructuredCrafterConfig;

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

/**
 * World that can provide an itemstack.
 * @author rubensworks
 */
public class WorldItemStackProvider implements IItemStackProvider {

    private static GameProfile PROFILE = new GameProfile(UUID.fromString("41C82C87-7AfB-4024-BB57-13D2C99CAE78"), "[StructuredCrafting]");
    private static final Map<ServerLevel, FakePlayer> FAKE_PLAYERS = new WeakHashMap<ServerLevel, FakePlayer>();

    public static FakePlayer getFakePlayer(ServerLevel world) {
        FakePlayer fakePlayer = FAKE_PLAYERS.get(world);
        if (fakePlayer == null) {
            fakePlayer = new FakePlayer(world, PROFILE);
            FAKE_PLAYERS.put(world, fakePlayer);
        }
        return fakePlayer;
    }

    @Override
    public boolean canProvideInput() {
        return BlockStructuredCrafterConfig.canTakeInputsFromWorld;
    }

    @Override
    public boolean canHandleOutput() {
        return BlockStructuredCrafterConfig.canPlaceOutputsIntoWorld;
    }

    @Override
    public boolean isValidForResults(Level world, BlockPos pos, Direction side) {
        return world.isEmptyBlock(pos);
    }

    protected boolean hasEmptyItemHandler(Level world, BlockPos pos, Direction side) {
        IItemHandler itemHandler = BlockEntityHelpers.getCapability(world, pos, side, Capabilities.ItemHandler.BLOCK).orElse(null);
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
    public boolean hasItemStack(Level world, BlockPos pos, Direction side) {
        return !world.isEmptyBlock(pos) && hasEmptyItemHandler(world, pos, side);
    }

    @Override
    public ItemStack getItemStack(Level world, BlockPos pos, Direction side) {
        BlockState blockState = world.getBlockState(pos);
        if(blockState != null && hasEmptyItemHandler(world, pos, side)) {
            return blockState.getCloneItemStack(new BlockHitResult(new Vec3(0, 0, 0), side, pos, false), world, pos, getFakePlayer((ServerLevel) world));
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean reduceItemStack(Level world, BlockPos pos, Direction side, boolean simulate) {
        boolean wasAir = world.getBlockState(pos).isAir();
        if(!simulate) {
            world.removeBlock(pos, false);
        }
        return !wasAir;
    }

    @Override
    public boolean addItemStack(Level world, BlockPos pos, Direction side, ItemStack itemStack, boolean simulate) {
        return setItemStack(world, pos, side, itemStack, simulate);
    }

    @Override
    public boolean setItemStack(Level world, BlockPos pos, Direction side, ItemStack itemStack, boolean simulate) {
        if(!simulate && itemStack.getItem() instanceof BlockItem) {
            world.setBlockAndUpdate(pos, ((BlockItem) itemStack.getItem()).getBlock().defaultBlockState());
            itemStack.shrink(1);
        }
        if(!simulate && itemStack.getCount() > 0) {
            ItemStackHelpers.spawnItemStack(world, pos, itemStack);
        }
        return true;
    }
}
