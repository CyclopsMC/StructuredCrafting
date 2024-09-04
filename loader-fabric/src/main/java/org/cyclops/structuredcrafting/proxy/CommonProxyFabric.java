package org.cyclops.structuredcrafting.proxy;

import org.cyclops.cyclopscore.init.ModBaseFabric;
import org.cyclops.cyclopscore.proxy.CommonProxyComponentFabric;
import org.cyclops.structuredcrafting.StructuredCraftingFabric;

/**
 * Proxy for server and client side.
 * @author rubensworks
 *
 */
public class CommonProxyFabric extends CommonProxyComponentFabric {

    @Override
    public ModBaseFabric<?> getMod() {
        return StructuredCraftingFabric._instance;
    }

}
