package org.cyclops.structuredcrafting;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.cyclops.cyclopscore.config.ConfigHandlerCommon;
import org.cyclops.cyclopscore.init.ModBaseForge;
import org.cyclops.cyclopscore.proxy.IClientProxyCommon;
import org.cyclops.cyclopscore.proxy.ICommonProxyCommon;
import org.cyclops.structuredcrafting.block.BlockStructuredCrafterConfig;
import org.cyclops.structuredcrafting.blockentity.BlockEntityStructuredCrafterConfig;
import org.cyclops.structuredcrafting.craft.provider.IItemStackProviderRegistry;
import org.cyclops.structuredcrafting.craft.provider.InventoryItemStackProviderForge;
import org.cyclops.structuredcrafting.craft.provider.ItemStackProviderRegistry;
import org.cyclops.structuredcrafting.craft.provider.WorldItemStackProviderForge;
import org.cyclops.structuredcrafting.proxy.ClientProxyForge;
import org.cyclops.structuredcrafting.proxy.CommonProxyForge;

/**
 * The main mod class of this mod.
 * @author rubensworks
 *
 */
@Mod(Reference.MOD_ID)
public class StructuredCraftingForge extends ModBaseForge<StructuredCraftingForge> implements IStructuredCraftingMod {

    /**
     * The unique instance of this mod.
     */
    public static StructuredCraftingForge _instance;

    public StructuredCraftingForge() {
        super(Reference.MOD_ID, (instance) -> {
            _instance = instance;
            IStructuredCraftingMod.MOD.set(instance);
        });
    }

    @Override
    protected void setup(FMLCommonSetupEvent event) {
        getRegistryManager().addRegistry(IItemStackProviderRegistry.class, new ItemStackProviderRegistry());

        super.setup(event);

        IItemStackProviderRegistry registry = getRegistryManager().getRegistry(IItemStackProviderRegistry.class);
        registry.registerProvider(new InventoryItemStackProviderForge(getModHelpers()));
        registry.registerProvider(new WorldItemStackProviderForge(getModHelpers()));
    }

    @Override
    protected IClientProxyCommon constructClientProxy() {
        return new ClientProxyForge();
    }

    @Override
    protected ICommonProxyCommon constructCommonProxy() {
        return new CommonProxyForge();
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
    protected void onConfigsRegister(ConfigHandlerCommon configHandler) {
        super.onConfigsRegister(configHandler);

        configHandler.addConfigurable(new GeneralConfig(this));

        configHandler.addConfigurable(new BlockStructuredCrafterConfig<>(this));
        configHandler.addConfigurable(new BlockEntityStructuredCrafterConfig<>(this));
    }
}
