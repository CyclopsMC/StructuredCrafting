package org.cyclops.structuredcrafting.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import org.cyclops.cyclopscore.config.ConfigurablePropertyCommon;
import org.cyclops.cyclopscore.config.ModConfigLocation;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;

/**
 * Config for {@link BlockStructuredCrafter}.
 * @author rubensworks
 */
public class BlockStructuredCrafterConfig<M extends IModBase> extends BlockConfigCommon<M> {

    /**
     * If crafting inputs can be taken from the world.
     */
    @ConfigurablePropertyCommon(category = "general", comment = "If crafting inputs can be taken from the world.", configLocation = ModConfigLocation.SERVER)
    public static boolean canTakeInputsFromWorld = true;

    /**
     * If crafting inputs can be taken from inventories.
     */
    @ConfigurablePropertyCommon(category = "general", comment = "If crafting inputs can be taken from inventories.", configLocation = ModConfigLocation.SERVER)
    public static boolean canTakeInputsFromInventory = true;

    /**
     * If crafting outputs can placed into the world.
     */
    @ConfigurablePropertyCommon(category = "general", comment = "If crafting outputs can placed into the world.", configLocation = ModConfigLocation.SERVER)
    public static boolean canPlaceOutputsIntoWorld = true;

    /**
     * If crafting outputs can placed into inventories.
     */
    @ConfigurablePropertyCommon(category = "general", comment = "If crafting outputs can placed into inventories.", configLocation = ModConfigLocation.SERVER)
    public static boolean canPlaceOutputsIntoInventory = true;

    /**
     * Make a new instance.
     */
    public BlockStructuredCrafterConfig(M mod) {
        super(
                mod,
                "structured_crafter",
                (eConfig) -> new BlockStructuredCrafter(Block.Properties.of()
                        .sound(SoundType.WOOD)
                        .strength(2.0f)),
                BlockConfigCommon.getDefaultItemConstructor(mod)
        );
    }

}
