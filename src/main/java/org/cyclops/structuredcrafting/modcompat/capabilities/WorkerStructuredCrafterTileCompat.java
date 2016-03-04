package org.cyclops.structuredcrafting.modcompat.capabilities;

import org.cyclops.commoncapabilities.api.capability.work.IWorker;
import org.cyclops.cyclopscore.modcompat.ICapabilityCompat;
import org.cyclops.structuredcrafting.Capabilities;
import org.cyclops.structuredcrafting.tileentity.TileStructuredCrafter;

/**
 * Compatibility for structured crafter worker capability.
 * @author rubensworks
 */
public class WorkerStructuredCrafterTileCompat implements ICapabilityCompat<TileStructuredCrafter> {

    @Override
    public void attach(final TileStructuredCrafter provider) {
        provider.addCapabilityInternal(Capabilities.WORKER, new Worker(provider));
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
            return provider.getWorld().isBlockPowered(provider.getPos());
        }
    }
}
