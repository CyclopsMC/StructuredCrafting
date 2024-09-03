package org.cyclops.structuredcrafting.blockentity;

import com.google.common.collect.Sets;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.cyclops.cyclopscore.config.extendedconfig.BlockEntityConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.structuredcrafting.RegistryEntries;

/**
 * Config for the {@link BlockEntityStructuredCrafter}.
 * @author rubensworks
 *
 */
public class BlockEntityStructuredCrafterConfig<M extends IModBase> extends BlockEntityConfigCommon<BlockEntityStructuredCrafter, M> {

    public BlockEntityStructuredCrafterConfig(M mod) {
        super(
                mod,
                "structured_crafter",
                (eConfig) -> new BlockEntityType<>(BlockEntityStructuredCrafter::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_STRUCTURED_CRAFTER.value()), null)
        );
    }

}
