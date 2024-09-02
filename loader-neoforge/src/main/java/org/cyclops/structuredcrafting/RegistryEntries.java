package org.cyclops.structuredcrafting;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.cyclops.structuredcrafting.blockentity.BlockEntityStructuredCrafter;

/**
 * Referenced registry entries.
 * @author rubensworks
 */
public class RegistryEntries {

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityStructuredCrafter>> BLOCK_ENTITY_STRUCTURED_CRAFTER = DeferredHolder.create(Registries.BLOCK_ENTITY_TYPE, ResourceLocation.parse("structuredcrafting:structured_crafter"));

}
