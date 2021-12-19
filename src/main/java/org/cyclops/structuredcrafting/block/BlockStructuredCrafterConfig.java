package org.cyclops.structuredcrafting.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.fml.config.ModConfig;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.structuredcrafting.StructuredCrafting;

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
     * If crafting inputs can be taken from the world.
     */
    @ConfigurableProperty(category = "general", comment = "If crafting inputs can be taken from the world.", configLocation = ModConfig.Type.SERVER)
    public static boolean canTakeInputsFromWorld = true;

    /**
     * If crafting inputs can be taken from inventories.
     */
    @ConfigurableProperty(category = "general", comment = "If crafting inputs can be taken from inventories.", configLocation = ModConfig.Type.SERVER)
    public static boolean canTakeInputsFromInventory = true;

    /**
     * If crafting outputs can placed into the world.
     */
    @ConfigurableProperty(category = "general", comment = "If crafting outputs can placed into the world.", configLocation = ModConfig.Type.SERVER)
    public static boolean canPlaceOutputsIntoWorld = true;

    /**
     * If crafting outputs can placed into inventories.
     */
    @ConfigurableProperty(category = "general", comment = "If crafting outputs can placed into inventories.", configLocation = ModConfig.Type.SERVER)
    public static boolean canPlaceOutputsIntoInventory = true;

    /**
     * Make a new instance.
     */
    public BlockStructuredCrafterConfig() {
        super(
                StructuredCrafting._instance,
                "structured_crafter",
                (eConfig) -> new BlockStructuredCrafter(Block.Properties.of(Material.STONE)
                        .strength(2.0f)),
                getDefaultItemConstructor(StructuredCrafting._instance)
        );
    }

}
