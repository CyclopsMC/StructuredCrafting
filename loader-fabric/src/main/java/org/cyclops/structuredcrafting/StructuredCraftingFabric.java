package org.cyclops.structuredcrafting;

import net.fabricmc.api.ModInitializer;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.cyclops.cyclopscore.config.ConfigHandlerCommon;
import org.cyclops.cyclopscore.init.ModBaseFabric;
import org.cyclops.cyclopscore.proxy.IClientProxyCommon;
import org.cyclops.cyclopscore.proxy.ICommonProxyCommon;
import org.cyclops.structuredcrafting.block.BlockStructuredCrafterConfig;
import org.cyclops.structuredcrafting.blockentity.BlockEntityStructuredCrafterConfig;
import org.cyclops.structuredcrafting.craft.provider.IItemStackProviderRegistry;
import org.cyclops.structuredcrafting.craft.provider.InventoryItemStackProviderFabric;
import org.cyclops.structuredcrafting.craft.provider.ItemStackProviderRegistry;
import org.cyclops.structuredcrafting.craft.provider.WorldItemStackProviderFabric;
import org.cyclops.structuredcrafting.proxy.ClientProxyFabric;
import org.cyclops.structuredcrafting.proxy.CommonProxyFabric;

/**
 * The main mod class of this mod.
 * @author rubensworks
 */
public class StructuredCraftingFabric extends ModBaseFabric<StructuredCraftingFabric> implements ModInitializer, IStructuredCraftingMod {

    /**
     * The unique instance of this mod.
     */
    public static StructuredCraftingFabric _instance;

    public StructuredCraftingFabric() {
        super(Reference.MOD_ID, (instance) -> {
            _instance = instance;
            IStructuredCraftingMod.MOD.set(instance);
        });
    }

    @Override
    protected IClientProxyCommon constructClientProxy() {
        return new ClientProxyFabric();
    }

    @Override
    protected ICommonProxyCommon constructCommonProxy() {
        return new CommonProxyFabric();
    }

    @Override
    protected boolean hasDefaultCreativeModeTab() {
        return true;
    }

    @Override
    protected CreativeModeTab.Builder constructDefaultCreativeModeTab(CreativeModeTab.Builder builder) {
        return super.constructDefaultCreativeModeTab(builder)
                .icon(() -> new ItemStack(RegistryEntries.ITEM_STRUCTURED_CRAFTER));
    }

    @Override
    public void onInitialize() {
        getRegistryManager().addRegistry(IItemStackProviderRegistry.class, new ItemStackProviderRegistry());

        super.onInitialize();

        IItemStackProviderRegistry registry = getRegistryManager().getRegistry(IItemStackProviderRegistry.class);
        registry.registerProvider(new InventoryItemStackProviderFabric());
        registry.registerProvider(new WorldItemStackProviderFabric());
    }

    @Override
    protected void onConfigsRegister(ConfigHandlerCommon configHandler) {
        super.onConfigsRegister(configHandler);

        configHandler.addConfigurable(new GeneralConfig(this));

        configHandler.addConfigurable(new BlockStructuredCrafterConfig<>(this));
        configHandler.addConfigurable(new BlockEntityStructuredCrafterConfig<>(this));
    }
}
