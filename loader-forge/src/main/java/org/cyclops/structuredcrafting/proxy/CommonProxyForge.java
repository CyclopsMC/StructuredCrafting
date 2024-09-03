package org.cyclops.structuredcrafting.proxy;

import org.cyclops.cyclopscore.init.ModBaseForge;
import org.cyclops.cyclopscore.proxy.CommonProxyComponentForge;
import org.cyclops.structuredcrafting.StructuredCraftingForge;

/**
 * Proxy for server and client side.
 * @author rubensworks
 *
 */
public class CommonProxyForge extends CommonProxyComponentForge {

    @Override
    public ModBaseForge<?> getMod() {
        return StructuredCraftingForge._instance;
    }

}
