package org.cyclops.structuredcrafting;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.config.ConfigHandler;
import org.cyclops.cyclopscore.init.ModBaseVersionable;
import org.cyclops.cyclopscore.modcompat.ModCompatLoader;
import org.cyclops.cyclopscore.proxy.IClientProxy;
import org.cyclops.cyclopscore.proxy.ICommonProxy;
import org.cyclops.structuredcrafting.block.BlockStructuredCrafterConfig;
import org.cyclops.structuredcrafting.blockentity.BlockEntityStructuredCrafter;
import org.cyclops.structuredcrafting.blockentity.TileStructuredCrafterConfig;
import org.cyclops.structuredcrafting.craft.provider.IItemStackProviderRegistry;
import org.cyclops.structuredcrafting.craft.provider.InventoryItemStackProvider;
import org.cyclops.structuredcrafting.craft.provider.ItemStackProviderRegistry;
import org.cyclops.structuredcrafting.craft.provider.WorldItemStackProvider;
import org.cyclops.structuredcrafting.modcompat.capabilities.WorkerStructuredCrafterTileCompat;

/**
 * The main mod class of StructuredCrafting.
 * @author rubensworks
 *
 */
@Mod(Reference.MOD_ID)
public class StructuredCrafting extends ModBaseVersionable<StructuredCrafting> {

    /**
     * The unique instance of this mod.
     */
    public static StructuredCrafting _instance;

    public StructuredCrafting() {
        super(Reference.MOD_ID, (instance) -> _instance = instance);
    }

    @Override
    protected void loadModCompats(ModCompatLoader modCompatLoader) {
        super.loadModCompats(modCompatLoader);

        // Capabilities
        getCapabilityConstructorRegistry().registerTile(BlockEntityStructuredCrafter.class, new WorkerStructuredCrafterTileCompat());
    }

    @Override
    protected void setup(FMLCommonSetupEvent event) {
        getRegistryManager().addRegistry(IItemStackProviderRegistry.class, new ItemStackProviderRegistry());

        super.setup(event);

        IItemStackProviderRegistry registry = getRegistryManager().getRegistry(IItemStackProviderRegistry.class);
        registry.registerProvider(new InventoryItemStackProvider());
        registry.registerProvider(new WorldItemStackProvider());
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

        configHandler.addConfigurable(new GeneralConfig());

        configHandler.addConfigurable(new BlockStructuredCrafterConfig());
        configHandler.addConfigurable(new TileStructuredCrafterConfig());
    }

    /**
     * Log a new info message for this mod.
     * @param message The message to show.
     */
    public static void clog(String message) {
        StructuredCrafting._instance.log(Level.INFO, message);
    }

    /**
     * Log a new message of the given level for this mod.
     * @param level The level in which the message must be shown.
     * @param message The message to show.
     */
    public static void clog(Level level, String message) {
        StructuredCrafting._instance.log(level, message);
    }
}
