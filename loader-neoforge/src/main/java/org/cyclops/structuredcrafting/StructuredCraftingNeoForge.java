package org.cyclops.structuredcrafting;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.config.ConfigHandler;
import org.cyclops.cyclopscore.init.ModBaseVersionable;
import org.cyclops.cyclopscore.modcompat.ModCompatLoader;
import org.cyclops.cyclopscore.proxy.IClientProxy;
import org.cyclops.cyclopscore.proxy.ICommonProxy;
import org.cyclops.structuredcrafting.block.BlockStructuredCrafterConfig;
import org.cyclops.structuredcrafting.blockentity.BlockEntityStructuredCrafterConfig;
import org.cyclops.structuredcrafting.craft.provider.IItemStackProviderRegistry;
import org.cyclops.structuredcrafting.craft.provider.InventoryItemStackProviderNeoForge;
import org.cyclops.structuredcrafting.craft.provider.ItemStackProviderRegistry;
import org.cyclops.structuredcrafting.craft.provider.WorldItemStackProviderNeoForge;
import org.cyclops.structuredcrafting.modcompat.capabilities.WorkerStructuredCrafterTileCompat;

/**
 * The main mod class of StructuredCrafting.
 * @author rubensworks
 *
 */
@Mod(Reference.MOD_ID)
public class StructuredCraftingNeoForge extends ModBaseVersionable<StructuredCraftingNeoForge> implements IStructuredCraftingMod {

    /**
     * The unique instance of this mod.
     */
    public static StructuredCraftingNeoForge _instance;

    public StructuredCraftingNeoForge(IEventBus modEventBus) {
        super(Reference.MOD_ID, (instance) -> {
            _instance = instance;
            IStructuredCraftingMod.MOD.set(instance);
        }, modEventBus);
    }

    @Override
    protected void loadModCompats(ModCompatLoader modCompatLoader) {
        super.loadModCompats(modCompatLoader);

        // Capabilities
        getCapabilityConstructorRegistry().registerBlockEntity(RegistryEntries.BLOCK_ENTITY_STRUCTURED_CRAFTER::value, new WorkerStructuredCrafterTileCompat());
    }

    @Override
    protected void setup(FMLCommonSetupEvent event) {
        getRegistryManager().addRegistry(IItemStackProviderRegistry.class, new ItemStackProviderRegistry());

        super.setup(event);

        IItemStackProviderRegistry registry = getRegistryManager().getRegistry(IItemStackProviderRegistry.class);
        registry.registerProvider(new InventoryItemStackProviderNeoForge());
        registry.registerProvider(new WorldItemStackProviderNeoForge());
    }

    @Override
    protected IClientProxy constructClientProxy() {
        return null;
    }

    @Override
    protected ICommonProxy constructCommonProxy() {
        return null;
    }

    @Override
    protected CreativeModeTab.Builder constructDefaultCreativeModeTab(CreativeModeTab.Builder builder) {
        return super.constructDefaultCreativeModeTab(builder)
                .icon(() -> new ItemStack(RegistryEntries.ITEM_STRUCTURED_CRAFTER));
    }

    @Override
    protected void onConfigsRegister(ConfigHandler configHandler) {
        super.onConfigsRegister(configHandler);

        configHandler.addConfigurable(new GeneralConfig(this));

        configHandler.addConfigurable(new BlockStructuredCrafterConfig<>(this));
        configHandler.addConfigurable(new BlockEntityStructuredCrafterConfig<>(this));
    }

    /**
     * Log a new info message for this mod.
     * @param message The message to show.
     */
    public static void clog(String message) {
        StructuredCraftingNeoForge._instance.log(Level.INFO, message);
    }

    /**
     * Log a new message of the given level for this mod.
     * @param level The level in which the message must be shown.
     * @param message The message to show.
     */
    public static void clog(Level level, String message) {
        StructuredCraftingNeoForge._instance.log(level, message);
    }
}
