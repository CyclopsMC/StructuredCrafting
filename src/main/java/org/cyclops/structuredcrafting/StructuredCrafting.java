package org.cyclops.structuredcrafting;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.config.ConfigHandler;
import org.cyclops.cyclopscore.config.extendedconfig.BlockItemConfigReference;
import org.cyclops.cyclopscore.init.ItemCreativeTab;
import org.cyclops.cyclopscore.init.ModBaseVersionable;
import org.cyclops.cyclopscore.init.RecipeHandler;
import org.cyclops.cyclopscore.modcompat.ModCompatLoader;
import org.cyclops.cyclopscore.proxy.ICommonProxy;
import org.cyclops.structuredcrafting.block.BlockStructuredCrafterConfig;
import org.cyclops.structuredcrafting.craft.provider.IItemStackProviderRegistry;
import org.cyclops.structuredcrafting.craft.provider.InventoryItemStackProvider;
import org.cyclops.structuredcrafting.craft.provider.ItemStackProviderRegistry;
import org.cyclops.structuredcrafting.craft.provider.WorldItemStackProvider;
import org.cyclops.structuredcrafting.modcompat.capabilities.WorkerStructuredCrafterTileCompat;
import org.cyclops.structuredcrafting.tileentity.TileStructuredCrafter;

/**
 * The main mod class of StructuredCrafting.
 * @author rubensworks
 *
 */
@Mod(modid = Reference.MOD_ID,
     name = Reference.MOD_NAME,
     useMetadata = true,
     version = Reference.MOD_VERSION,
     dependencies = Reference.MOD_DEPENDENCIES,
     guiFactory = "org.cyclops.structuredcrafting.GuiConfigOverview$ExtendedConfigGuiFactory"
)
public class StructuredCrafting extends ModBaseVersionable {

    /**
     * The unique instance of this mod.
     */
    @Mod.Instance(value = Reference.MOD_ID)
    public static StructuredCrafting _instance;

    public StructuredCrafting() {
        super(Reference.MOD_ID, Reference.MOD_NAME, Reference.MOD_VERSION);
    }

    @Override
    protected RecipeHandler constructRecipeHandler() {
        return new RecipeHandler(this, "recipes.xml");
    }

    @Override
    protected void loadModCompats(ModCompatLoader modCompatLoader) {
        super.loadModCompats(modCompatLoader);

        // Capabilities
        getCapabilityConstructorRegistry().registerTile(TileStructuredCrafter.class, new WorkerStructuredCrafterTileCompat());
    }

    @Mod.EventHandler
    @Override
    public final void preInit(FMLPreInitializationEvent event) {
        getRegistryManager().addRegistry(IItemStackProviderRegistry.class, new ItemStackProviderRegistry());

        super.preInit(event);
    }

    @Mod.EventHandler
    @Override
    public final void init(FMLInitializationEvent event) {
        super.init(event);

        IItemStackProviderRegistry registry = getRegistryManager().getRegistry(IItemStackProviderRegistry.class);
        registry.registerProvider(new InventoryItemStackProvider());
        registry.registerProvider(new WorldItemStackProvider());
    }

    @Mod.EventHandler
    @Override
    public final void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

    @Mod.EventHandler
    @Override
    public void onServerStarted(FMLServerStartedEvent event) {
        super.onServerStarted(event);
    }

    @Mod.EventHandler
    @Override
    public void onServerStopping(FMLServerStoppingEvent event) {
        super.onServerStopping(event);
    }

    @Override
    public CreativeTabs constructDefaultCreativeTab() {
        return new ItemCreativeTab(this, new BlockItemConfigReference(BlockStructuredCrafterConfig.class));
    }

    @Override
    public void onGeneralConfigsRegister(ConfigHandler configHandler) {
        configHandler.add(new GeneralConfig());
    }

    @Override
    public void onMainConfigsRegister(ConfigHandler configHandler) {
        Configs.registerBlocks(configHandler);
    }

    @Override
    public ICommonProxy getProxy() {
        return null;
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