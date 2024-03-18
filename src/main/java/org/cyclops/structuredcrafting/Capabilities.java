package org.cyclops.structuredcrafting;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;

/**
 * Used capabilities for this mod.
 * @author rubensworks
 */
public class Capabilities {
    public static BlockCapability<IWorker, Direction> WORKER = BlockCapability.createSided(new ResourceLocation("commoncapabilities", "worker"), IWorker.class);
}
