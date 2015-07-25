package org.cyclops.structuredcrafting;

import org.cyclops.cyclopscore.config.ConfigHandler;
import org.cyclops.structuredcrafting.block.BlockStructuredCrafterConfig;

/**
 * This class holds a set of all the configs that need to be registered.
 * @author rubensworks
 */
public class Configs {

    public static void registerBlocks(ConfigHandler configHandler) {

        // Blocks
        configHandler.add(new BlockStructuredCrafterConfig());

    }

}
