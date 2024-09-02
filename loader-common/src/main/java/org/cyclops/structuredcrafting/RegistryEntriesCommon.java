package org.cyclops.structuredcrafting;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.cyclops.cyclopscore.config.DeferredHolderCommon;

/**
 * Referenced registry entries.
 * @author rubensworks
 */
public class RegistryEntriesCommon {

    public static final DeferredHolderCommon<Item, Item> ITEM_STRUCTURED_CRAFTER = DeferredHolderCommon.create(Registries.ITEM, ResourceLocation.parse("structuredcrafting:structured_crafter"));

    public static final DeferredHolderCommon<Block, Block> BLOCK_STRUCTURED_CRAFTER = DeferredHolderCommon.create(Registries.BLOCK, ResourceLocation.parse("structuredcrafting:structured_crafter"));

    // TODO: when when move the following, rename this file
//    public static final DeferredHolderCommon<BlockEntityType<?>, BlockEntityType<?>> BLOCK_ENTITY_STRUCTURED_CRAFTER = DeferredHolderCommon.create(Registries.BLOCK_ENTITY_TYPE, ResourceLocation.parse("structuredcrafting:structured_crafter"));

}
