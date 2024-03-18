package org.cyclops.structuredcrafting;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.cyclops.structuredcrafting.blockentity.BlockEntityStructuredCrafter;

/**
 * Referenced registry entries.
 * @author rubensworks
 */
public class RegistryEntries {

    public static final DeferredHolder<Item, Item> ITEM_STRUCTURED_CRAFTER = DeferredHolder.create(Registries.ITEM, new ResourceLocation("structuredcrafting:structured_crafter"));

    public static final DeferredHolder<Block, Block> BLOCK_STRUCTURED_CRAFTER = DeferredHolder.create(Registries.BLOCK, new ResourceLocation("structuredcrafting:structured_crafter"));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BlockEntityStructuredCrafter>> BLOCK_ENTITY_STRUCTURED_CRAFTER = DeferredHolder.create(Registries.BLOCK_ENTITY_TYPE, new ResourceLocation("structuredcrafting:structured_crafter"));

}
