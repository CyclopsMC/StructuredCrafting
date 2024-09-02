package org.cyclops.structuredcrafting.blockentity;

import com.google.common.collect.Sets;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.cyclops.cyclopscore.config.extendedconfig.BlockEntityConfig;
import org.cyclops.structuredcrafting.RegistryEntries;
import org.cyclops.structuredcrafting.StructuredCrafting;

/**
 * Config for the {@link BlockEntityStructuredCrafter}.
 * @author rubensworks
 *
 */
public class BlockEntityStructuredCrafterConfig extends BlockEntityConfig<BlockEntityStructuredCrafter> {

    public BlockEntityStructuredCrafterConfig() {
        super(
                StructuredCrafting._instance,
                "structured_crafter",
                (eConfig) -> new BlockEntityType<>(BlockEntityStructuredCrafter::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_STRUCTURED_CRAFTER.get()), null)
        );
    }

}
