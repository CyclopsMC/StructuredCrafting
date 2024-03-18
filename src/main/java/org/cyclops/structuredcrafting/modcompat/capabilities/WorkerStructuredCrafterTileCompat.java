package org.cyclops.structuredcrafting.modcompat.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.BaseCapability;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;
import org.cyclops.cyclopscore.modcompat.capabilities.ICapabilityConstructor;
import org.cyclops.structuredcrafting.Capabilities;
import org.cyclops.structuredcrafting.blockentity.BlockEntityStructuredCrafter;

import javax.annotation.Nullable;

/**
 * Compatibility for structured crafter worker capability.
 * @author rubensworks
 */
public class WorkerStructuredCrafterTileCompat implements ICapabilityConstructor<BlockEntityStructuredCrafter, Direction, IWorker, BlockEntityType<BlockEntityStructuredCrafter>> {

    @Nullable
    @Override
    public ICapabilityProvider<BlockEntityStructuredCrafter, Direction, IWorker> createProvider(BlockEntityType<BlockEntityStructuredCrafter> capabilityKey) {
        return (blockEntity, side) -> new Worker(blockEntity);
    }

    @Override
    public BaseCapability<IWorker, Direction> getCapability() {
        return Capabilities.WORKER;
    }

    public static class Worker implements IWorker {

        private final BlockEntityStructuredCrafter provider;

        public Worker(BlockEntityStructuredCrafter provider) {
            this.provider = provider;
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean hasWork() {
            return provider.getMatrix().craft(true);
        }

        @Override
        public boolean canWork() {
            return provider.getLevel().hasNeighborSignal(provider.getBlockPos());
        }
    }
}
