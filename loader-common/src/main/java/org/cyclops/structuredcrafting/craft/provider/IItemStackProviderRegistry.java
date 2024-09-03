package org.cyclops.structuredcrafting.craft.provider;

import org.cyclops.cyclopscore.init.IRegistry;

import java.util.List;

/**
 * Registry for {@link IItemStackProvider}.
 * @author rubensworks
 */
public interface IItemStackProviderRegistry extends IRegistry {

    public void registerProvider(IItemStackProvider provider);

    public List<IItemStackProvider> getProviders();

}
