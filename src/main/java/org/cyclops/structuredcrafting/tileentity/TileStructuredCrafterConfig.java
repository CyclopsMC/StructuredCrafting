package org.cyclops.structuredcrafting.tileentity;

import com.google.common.collect.Sets;
import net.minecraft.tileentity.TileEntityType;
import org.cyclops.cyclopscore.config.extendedconfig.TileEntityConfig;
import org.cyclops.structuredcrafting.RegistryEntries;
import org.cyclops.structuredcrafting.StructuredCrafting;

/**
 * Config for the {@link TileStructuredCrafter}.
 * @author rubensworks
 *
 */
public class TileStructuredCrafterConfig extends TileEntityConfig<TileStructuredCrafter> {

    public TileStructuredCrafterConfig() {
        super(
                StructuredCrafting._instance,
                "structured_crafter",
                (eConfig) -> new TileEntityType<>(TileStructuredCrafter::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_STRUCTURED_CRAFTER), null)
        );
    }

}
