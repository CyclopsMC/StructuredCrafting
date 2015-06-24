package org.cyclops.structurecrafting.block;

import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.structurecrafting.StructuredCrafting;

/**
 * Config for {@link BlockStructuredCrafter}.
 * @author rubensworks
 */
public class BlockStructuredCrafterConfig extends BlockConfig {

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
