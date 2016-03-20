package org.cyclops.structuredcrafting.block;

import com.google.common.collect.Lists;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.block.property.BlockProperty;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlockContainer;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.structuredcrafting.tileentity.TileStructuredCrafter;

/**
 * This block will detect neighbour block updates and will try to craft a new block/item from them.
 * @author rubensworks
 */
public class BlockStructuredCrafter extends ConfigurableBlockContainer {

    @BlockProperty
    public static final PropertyDirection FACING = PropertyDirection.create("facing", Lists.newArrayList(EnumFacing.VALUES));

    private static BlockStructuredCrafter _instance = null;

    /**
     * Get the unique instance.
     * @return The instance.
     */
    public static BlockStructuredCrafter getInstance() {
        return _instance;
    }

    /**
     * Make a new block instance.
     * @param eConfig Config for this block.
     */
    public BlockStructuredCrafter(ExtendedConfig eConfig) {
        super(eConfig, Material.ground, TileStructuredCrafter.class);

        setHardness(2.0F);
        setStepSound(SoundType.STONE);
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ,
                                     int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, facing.getOpposite());
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(playerIn != null && heldItem != null && heldItem.getItem() == Items.stick) {
            this.rotateBlock(worldIn, pos, side);
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
    }

}
