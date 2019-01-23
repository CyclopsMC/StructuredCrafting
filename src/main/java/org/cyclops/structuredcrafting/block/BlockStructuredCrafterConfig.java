package org.cyclops.structuredcrafting.block;

import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
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
     * If crafting inputs can be taken from the world.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.GENERAL, comment = "If crafting inputs can be taken from the world.")
    public static boolean canTakeInputsFromWorld = true;

    /**
     * If crafting inputs can be taken from inventories.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.GENERAL, comment = "If crafting inputs can be taken from inventories.")
    public static boolean canTakeInputsFromInventory = true;

    /**
     * If crafting outputs can placed into the world.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.GENERAL, comment = "If crafting outputs can placed into the world.")
    public static boolean canPlaceOutputsIntoWorld = true;

    /**
     * If crafting outputs can placed into inventories.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.GENERAL, comment = "If crafting outputs can placed into inventories.")
    public static boolean canPlaceOutputsIntoInventory = true;

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
