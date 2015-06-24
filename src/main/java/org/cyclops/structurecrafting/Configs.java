package org.cyclops.structurecrafting;

import org.cyclops.cyclopscore.config.ConfigHandler;
import org.cyclops.structurecrafting.block.BlockStructuredCrafterConfig;

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
