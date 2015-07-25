package org.cyclops.structuredcrafting.craft.provider;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Registry for {@link IItemStackProvider}.
 * @author rubensworks
 */
public class ItemStackProviderRegistry implements IItemStackProviderRegistry {

    private final List<IItemStackProvider> providers = Lists.newLinkedList();

    @Override
    public void registerProvider(IItemStackProvider provider) {
        providers.add(provider);
    }

    @Override
    public List<IItemStackProvider> getProviders() {
        return providers;
    }

}
