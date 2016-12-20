package org.cyclops.structuredcrafting.block;

import org.cyclops.cyclopscore.config.extendedconfig.BlockContainerConfig;
import org.cyclops.structuredcrafting.StructuredCrafting;

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
            "structured_crafter",
            null,
            BlockStructuredCrafter.class
        );
    }

    @Override
    public boolean isDisableable() {
        return false;
    }

}
