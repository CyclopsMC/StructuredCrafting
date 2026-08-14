package org.cyclops.structuredcrafting.craft.provider;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.items.IItemHandler;
import org.cyclops.cyclopscore.helper.BlockEntityHelpers;

import com.google.common.collect.MapMaker;

import java.util.Map;

/**
 * World that can provide an itemstack.
 * @author rubensworks
 */
public class WorldItemStackProviderNeoForge extends WorldItemStackProviderBase {

    // Weak keys AND values: the FakePlayer value holds a reference to its ServerLevel key,
    // so a WeakHashMap (weak keys only) would keep the key alive forever and leak.
    private static final Map<ServerLevel, FakePlayer> FAKE_PLAYERS = new MapMaker().weakKeys().weakValues().makeMap();

    public static FakePlayer getFakePlayer(ServerLevel world) {
        FakePlayer fakePlayer = FAKE_PLAYERS.get(world);
        if (fakePlayer == null) {
            fakePlayer = new FakePlayer(world, PROFILE);
            FAKE_PLAYERS.put(world, fakePlayer);
        }
        return fakePlayer;
    }

    @Override
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
    public ItemStack getItemStack(Level world, BlockPos pos, Direction side) {
        BlockState blockState = world.getBlockState(pos);
        if(blockState != null && hasEmptyItemHandler(world, pos, side)) {
            return blockState.getCloneItemStack(new BlockHitResult(new Vec3(0, 0, 0), side, pos, false), world, pos, getFakePlayer((ServerLevel) world));
        }
        return ItemStack.EMPTY;
    }

}
