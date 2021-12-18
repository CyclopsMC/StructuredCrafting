package org.cyclops.structuredcrafting.modcompat.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;
import org.cyclops.cyclopscore.modcompat.capabilities.DefaultCapabilityProvider;
import org.cyclops.cyclopscore.modcompat.capabilities.SimpleCapabilityConstructor;
import org.cyclops.structuredcrafting.Capabilities;
import org.cyclops.structuredcrafting.tileentity.TileStructuredCrafter;

import javax.annotation.Nullable;

/**
 * Compatibility for structured crafter worker capability.
 * @author rubensworks
 */
public class WorkerStructuredCrafterTileCompat extends SimpleCapabilityConstructor<IWorker, TileStructuredCrafter> {

    @Nullable
    @Override
    public ICapabilityProvider createProvider(TileStructuredCrafter host) {
        return new DefaultCapabilityProvider<>(Capabilities.WORKER, new Worker(host));
    }

    @Override
    public Capability<IWorker> getCapability() {
        return Capabilities.WORKER;
    }

    public static class Worker implements IWorker {

        private final TileStructuredCrafter provider;

        public Worker(TileStructuredCrafter provider) {
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
