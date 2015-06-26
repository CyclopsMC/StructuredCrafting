package org.cyclops.structurecrafting.block;

import org.cyclops.cyclopscore.config.extendedconfig.BlockContainerConfig;
import org.cyclops.structurecrafting.StructuredCrafting;

/**
 * Config for {@link BlockStructuredCrafter}.
 * @author rubensworks
 */
public class BlockStructuredCrafterConfig extends BlockContainerConfig {

    /**
     * The unique instance.
     */
    public static BlockStructuredCrafterConfig _instance;

    /**
     * Make a new instance.
     */
    public BlockStructuredCrafterConfig() {
        super(
            StructuredCrafting._instance,
            true,
            "structuredCrafter",
            null,
            BlockStructuredCrafter.class
        );
    }

    @Override
    public boolean isDisableable() {
        return false;
    }

}
