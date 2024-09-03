package org.cyclops.structuredcrafting.proxy;

import org.cyclops.cyclopscore.init.ModBaseForge;
import org.cyclops.cyclopscore.proxy.ClientProxyComponentForge;
import org.cyclops.structuredcrafting.StructuredCraftingForge;

/**
 * Proxy for the client side.
 *
 * @author rubensworks
 *
 */
public class ClientProxyForge extends ClientProxyComponentForge {

    public ClientProxyForge() {
        super(new CommonProxyForge());
    }

    @Override
    public ModBaseForge<?> getMod() {
        return StructuredCraftingForge._instance;
    }

}
