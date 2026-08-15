package org.cyclops.structuredcrafting.craft.provider;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import org.cyclops.cyclopscore.helper.IModHelpersNeoForge;

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
        ResourceHandler<ItemResource> itemHandler = IModHelpersNeoForge.get().getCapabilityHelpers().getCapability(world, pos, side, Capabilities.Item.BLOCK).orElse(null);
        boolean emptyItemHandler = true;
        if (itemHandler != null) {
            for (int i = 0; i < itemHandler.size(); i++) {
                if (!itemHandler.getResource(i).isEmpty()) {
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
        if (!blockState.isAir() && hasEmptyItemHandler(world, pos, side)) {
            return blockState.getCloneItemStack(pos, world, true, getFakePlayer((ServerLevel) world));
        }
        return ItemStack.EMPTY;
    }

}
