package org.cyclops.structuredcrafting;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.cyclops.cyclopscore.config.DeferredHolderCommon;
import org.cyclops.structuredcrafting.blockentity.BlockEntityStructuredCrafter;

/**
 * Referenced registry entries.
 * @author rubensworks
 */
public class RegistryEntries {

    public static final DeferredHolderCommon<Item, Item> ITEM_STRUCTURED_CRAFTER = DeferredHolderCommon.create(Registries.ITEM, ResourceLocation.parse("structuredcrafting:structured_crafter"));

    public static final DeferredHolderCommon<Block, Block> BLOCK_STRUCTURED_CRAFTER = DeferredHolderCommon.create(Registries.BLOCK, ResourceLocation.parse("structuredcrafting:structured_crafter"));

    public static final DeferredHolderCommon<BlockEntityType<?>, BlockEntityType<BlockEntityStructuredCrafter>> BLOCK_ENTITY_STRUCTURED_CRAFTER = DeferredHolderCommon.create(Registries.BLOCK_ENTITY_TYPE, ResourceLocation.parse("structuredcrafting:structured_crafter"));

}
