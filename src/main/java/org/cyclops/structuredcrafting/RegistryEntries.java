package org.cyclops.structuredcrafting;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.ObjectHolder;
import org.cyclops.structuredcrafting.blockentity.BlockEntityStructuredCrafter;

/**
 * Referenced registry entries.
 * @author rubensworks
 */
public class RegistryEntries {

    @ObjectHolder("structuredcrafting:structured_crafter")
    public static final Item ITEM_STRUCTURED_CRAFTER = null;

    @ObjectHolder("structuredcrafting:structured_crafter")
    public static final Block BLOCK_STRUCTURED_CRAFTER = null;

    @ObjectHolder("structuredcrafting:structured_crafter")
    public static final BlockEntityType<BlockEntityStructuredCrafter> BLOCK_ENTITY_STRUCTURED_CRAFTER = null;

}
