package org.cyclops.structuredcrafting.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.minecraft.world.level.block.entity.BannerPatterns;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import org.cyclops.structuredcrafting.Reference;
import org.cyclops.structuredcrafting.RegistryEntries;
import org.cyclops.structuredcrafting.block.BlockStructuredCrafter;

/**
 * @author rubensworks
 */
public class GameTestsCommon {

    public static final String TEMPLATE_EMPTY = Reference.MOD_ID + ":empty10";
    public static final BlockPos POS = BlockPos.ZERO;

    @GameTest(template = TEMPLATE_EMPTY)
    public void testPlacementDirection(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ItemStack itemStack = new ItemStack(RegistryEntries.ITEM_STRUCTURED_CRAFTER.value());
        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);
        helper.placeAt(player, itemStack, POS, Direction.SOUTH);

        helper.succeedIf(() -> {
            helper.assertBlockPresent(RegistryEntries.BLOCK_STRUCTURED_CRAFTER.value(), POS.south());
            helper.assertBlockProperty(POS.south(), BlockStructuredCrafter.FACING, Direction.NORTH);
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testCraftFromWorldToWorldStairs(GameTestHelper helper) {
        helper.setBlock(POS.offset(2, 2, 2), RegistryEntries.BLOCK_STRUCTURED_CRAFTER.value()
                .defaultBlockState()
                .setValue(BlockStructuredCrafter.FACING, Direction.NORTH));

        // Define inputs
        helper.setBlock(POS.offset(3, 1, 3), Blocks.OAK_PLANKS);
        helper.setBlock(POS.offset(2, 1, 3), Blocks.OAK_PLANKS);
        helper.setBlock(POS.offset(1, 1, 3), Blocks.OAK_PLANKS);
        helper.setBlock(POS.offset(3, 2, 3), Blocks.OAK_PLANKS);
        helper.setBlock(POS.offset(2, 2, 3), Blocks.OAK_PLANKS);
        helper.setBlock(POS.offset(3, 3, 3), Blocks.OAK_PLANKS);

        // Activate crafter
        helper.setBlock(POS.offset(1, 2, 2), Blocks.REDSTONE_BLOCK);

        helper.succeedWhen(() -> {
            // Result
            helper.assertBlockPresent(Blocks.OAK_STAIRS, POS.offset(2, 2, 1));

            // Inputs must be consumed
            helper.assertBlockNotPresent(Blocks.OAK_PLANKS, POS.offset(3, 1, 3));
            helper.assertBlockNotPresent(Blocks.OAK_PLANKS, POS.offset(2, 1, 3));
            helper.assertBlockNotPresent(Blocks.OAK_PLANKS, POS.offset(1, 1, 3));
            helper.assertBlockNotPresent(Blocks.OAK_PLANKS, POS.offset(3, 2, 3));
            helper.assertBlockNotPresent(Blocks.OAK_PLANKS, POS.offset(2, 2, 3));
            helper.assertBlockNotPresent(Blocks.OAK_PLANKS, POS.offset(3, 3, 3));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testCraftFromWorldToWorldChest(GameTestHelper helper) {
        helper.setBlock(POS.offset(2, 2, 2), RegistryEntries.BLOCK_STRUCTURED_CRAFTER.value()
                .defaultBlockState()
                .setValue(BlockStructuredCrafter.FACING, Direction.NORTH));

        // Define inputs
        helper.setBlock(POS.offset(3, 1, 3), Blocks.OAK_PLANKS);
        helper.setBlock(POS.offset(2, 1, 3), Blocks.OAK_PLANKS);
        helper.setBlock(POS.offset(1, 1, 3), Blocks.OAK_PLANKS);
        helper.setBlock(POS.offset(3, 2, 3), Blocks.OAK_PLANKS);
        helper.setBlock(POS.offset(1, 2, 3), Blocks.OAK_PLANKS);
        helper.setBlock(POS.offset(3, 3, 3), Blocks.OAK_PLANKS);
        helper.setBlock(POS.offset(2, 3, 3), Blocks.OAK_PLANKS);
        helper.setBlock(POS.offset(1, 3, 3), Blocks.OAK_PLANKS);

        // Activate crafter
        helper.setBlock(POS.offset(1, 2, 2), Blocks.REDSTONE_BLOCK);

        helper.succeedWhen(() -> {
            // Result
            helper.assertBlockPresent(Blocks.CHEST, POS.offset(2, 2, 1));

            // Inputs must be consumed
            helper.assertBlockNotPresent(Blocks.OAK_PLANKS, POS.offset(3, 1, 3));
            helper.assertBlockNotPresent(Blocks.OAK_PLANKS, POS.offset(2, 1, 3));
            helper.assertBlockNotPresent(Blocks.OAK_PLANKS, POS.offset(1, 1, 3));
            helper.assertBlockNotPresent(Blocks.OAK_PLANKS, POS.offset(3, 2, 3));
            helper.assertBlockNotPresent(Blocks.OAK_PLANKS, POS.offset(1, 2, 3));
            helper.assertBlockNotPresent(Blocks.OAK_PLANKS, POS.offset(3, 3, 3));
            helper.assertBlockNotPresent(Blocks.OAK_PLANKS, POS.offset(2, 3, 3));
            helper.assertBlockNotPresent(Blocks.OAK_PLANKS, POS.offset(1, 3, 3));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testCraftFromWorldToWorldChestTags(GameTestHelper helper) {
        helper.setBlock(POS.offset(2, 2, 2), RegistryEntries.BLOCK_STRUCTURED_CRAFTER.value()
                .defaultBlockState()
                .setValue(BlockStructuredCrafter.FACING, Direction.NORTH));

        // Define inputs
        helper.setBlock(POS.offset(3, 1, 3), Blocks.OAK_PLANKS);
        helper.setBlock(POS.offset(2, 1, 3), Blocks.BIRCH_PLANKS);
        helper.setBlock(POS.offset(1, 1, 3), Blocks.OAK_PLANKS);
        helper.setBlock(POS.offset(3, 2, 3), Blocks.OAK_PLANKS);
        helper.setBlock(POS.offset(1, 2, 3), Blocks.OAK_PLANKS);
        helper.setBlock(POS.offset(3, 3, 3), Blocks.BAMBOO_PLANKS);
        helper.setBlock(POS.offset(2, 3, 3), Blocks.OAK_PLANKS);
        helper.setBlock(POS.offset(1, 3, 3), Blocks.OAK_PLANKS);

        // Activate crafter
        helper.setBlock(POS.offset(1, 2, 2), Blocks.REDSTONE_BLOCK);

        helper.succeedWhen(() -> {
            // Result
            helper.assertBlockPresent(Blocks.CHEST, POS.offset(2, 2, 1));

            // Inputs must be consumed
            helper.assertBlockNotPresent(Blocks.OAK_PLANKS, POS.offset(3, 1, 3));
            helper.assertBlockNotPresent(Blocks.BIRCH_PLANKS, POS.offset(2, 1, 3));
            helper.assertBlockNotPresent(Blocks.OAK_PLANKS, POS.offset(1, 1, 3));
            helper.assertBlockNotPresent(Blocks.OAK_PLANKS, POS.offset(3, 2, 3));
            helper.assertBlockNotPresent(Blocks.OAK_PLANKS, POS.offset(1, 2, 3));
            helper.assertBlockNotPresent(Blocks.BAMBOO_PLANKS, POS.offset(3, 3, 3));
            helper.assertBlockNotPresent(Blocks.OAK_PLANKS, POS.offset(2, 3, 3));
            helper.assertBlockNotPresent(Blocks.OAK_PLANKS, POS.offset(1, 3, 3));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testCraftFromChestsToWorldStairs(GameTestHelper helper) {
        helper.setBlock(POS.offset(2, 2, 2), RegistryEntries.BLOCK_STRUCTURED_CRAFTER.value()
                .defaultBlockState()
                .setValue(BlockStructuredCrafter.FACING, Direction.NORTH));

        // Define inputs
        setChestWithItem(helper, POS.offset(3, 1, 3), new ItemStack(Blocks.OAK_PLANKS));
        setChestWithItem(helper, POS.offset(2, 1, 3), new ItemStack(Blocks.OAK_PLANKS));
        setChestWithItem(helper, POS.offset(1, 1, 3), new ItemStack(Blocks.OAK_PLANKS));
        setChestWithItem(helper, POS.offset(3, 2, 3), new ItemStack(Blocks.OAK_PLANKS));
        setChestWithItem(helper, POS.offset(2, 2, 3), new ItemStack(Blocks.OAK_PLANKS));
        setChestWithItem(helper, POS.offset(3, 3, 3), new ItemStack(Blocks.OAK_PLANKS));

        // Activate crafter
        helper.setBlock(POS.offset(1, 2, 2), Blocks.REDSTONE_BLOCK);

        helper.succeedWhen(() -> {
            // Result
            helper.assertBlockPresent(Blocks.OAK_STAIRS, POS.offset(2, 2, 1));

            // Inputs must be consumed
            assertChestEmpty(helper, POS.offset(3, 1, 3));
            assertChestEmpty(helper, POS.offset(2, 1, 3));
            assertChestEmpty(helper, POS.offset(1, 1, 3));
            assertChestEmpty(helper, POS.offset(3, 2, 3));
            assertChestEmpty(helper, POS.offset(2, 2, 3));
            assertChestEmpty(helper, POS.offset(3, 3, 3));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testCraftFromWorldToChestStairs(GameTestHelper helper) {
        helper.setBlock(POS.offset(2, 2, 2), RegistryEntries.BLOCK_STRUCTURED_CRAFTER.value()
                .defaultBlockState()
                .setValue(BlockStructuredCrafter.FACING, Direction.NORTH));

        // Define inputs
        helper.setBlock(POS.offset(3, 1, 3), Blocks.OAK_PLANKS);
        helper.setBlock(POS.offset(2, 1, 3), Blocks.OAK_PLANKS);
        helper.setBlock(POS.offset(1, 1, 3), Blocks.OAK_PLANKS);
        helper.setBlock(POS.offset(3, 2, 3), Blocks.OAK_PLANKS);
        helper.setBlock(POS.offset(2, 2, 3), Blocks.OAK_PLANKS);
        helper.setBlock(POS.offset(3, 3, 3), Blocks.OAK_PLANKS);

        // Set output chest
        helper.setBlock(POS.offset(2, 2, 1), Blocks.CHEST);

        // Activate crafter
        helper.setBlock(POS.offset(1, 2, 2), Blocks.REDSTONE_BLOCK);

        helper.succeedWhen(() -> {
            // Result
            assertChestContains(helper, POS.offset(2, 2, 1), new ItemStack(Blocks.OAK_STAIRS));

            // Inputs must be consumed
            helper.assertBlockNotPresent(Blocks.OAK_PLANKS, POS.offset(3, 1, 3));
            helper.assertBlockNotPresent(Blocks.OAK_PLANKS, POS.offset(2, 1, 3));
            helper.assertBlockNotPresent(Blocks.OAK_PLANKS, POS.offset(1, 1, 3));
            helper.assertBlockNotPresent(Blocks.OAK_PLANKS, POS.offset(3, 2, 3));
            helper.assertBlockNotPresent(Blocks.OAK_PLANKS, POS.offset(2, 2, 3));
            helper.assertBlockNotPresent(Blocks.OAK_PLANKS, POS.offset(3, 3, 3));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testCraftFromChestsToChestStairs(GameTestHelper helper) {
        helper.setBlock(POS.offset(2, 2, 2), RegistryEntries.BLOCK_STRUCTURED_CRAFTER.value()
                .defaultBlockState()
                .setValue(BlockStructuredCrafter.FACING, Direction.NORTH));

        // Define inputs
        setChestWithItem(helper, POS.offset(3, 1, 3), new ItemStack(Blocks.OAK_PLANKS));
        setChestWithItem(helper, POS.offset(2, 1, 3), new ItemStack(Blocks.OAK_PLANKS));
        setChestWithItem(helper, POS.offset(1, 1, 3), new ItemStack(Blocks.OAK_PLANKS));
        setChestWithItem(helper, POS.offset(3, 2, 3), new ItemStack(Blocks.OAK_PLANKS));
        setChestWithItem(helper, POS.offset(2, 2, 3), new ItemStack(Blocks.OAK_PLANKS));
        setChestWithItem(helper, POS.offset(3, 3, 3), new ItemStack(Blocks.OAK_PLANKS));

        // Set output chest
        helper.setBlock(POS.offset(2, 2, 1), Blocks.CHEST);

        // Activate crafter
        helper.setBlock(POS.offset(1, 2, 2), Blocks.REDSTONE_BLOCK);

        helper.succeedWhen(() -> {
            // Result
            assertChestContains(helper, POS.offset(2, 2, 1), new ItemStack(Blocks.OAK_STAIRS));

            // Inputs must be consumed
            assertChestEmpty(helper, POS.offset(3, 1, 3));
            assertChestEmpty(helper, POS.offset(2, 1, 3));
            assertChestEmpty(helper, POS.offset(1, 1, 3));
            assertChestEmpty(helper, POS.offset(3, 2, 3));
            assertChestEmpty(helper, POS.offset(2, 2, 3));
            assertChestEmpty(helper, POS.offset(3, 3, 3));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testCraftFromChestsToChestCakeReusable(GameTestHelper helper) {
        helper.setBlock(POS.offset(2, 2, 2), RegistryEntries.BLOCK_STRUCTURED_CRAFTER.value()
                .defaultBlockState()
                .setValue(BlockStructuredCrafter.FACING, Direction.NORTH));

        // Define inputs
        setChestWithItem(helper, POS.offset(3, 3, 3), new ItemStack(Items.MILK_BUCKET));
        setChestWithItem(helper, POS.offset(2, 3, 3), new ItemStack(Items.MILK_BUCKET));
        setChestWithItem(helper, POS.offset(1, 3, 3), new ItemStack(Items.MILK_BUCKET));
        setChestWithItem(helper, POS.offset(3, 2, 3), new ItemStack(Items.SUGAR));
        setChestWithItem(helper, POS.offset(2, 2, 3), new ItemStack(Items.EGG));
        setChestWithItem(helper, POS.offset(1, 2, 3), new ItemStack(Items.SUGAR));
        setChestWithItem(helper, POS.offset(3, 1, 3), new ItemStack(Items.WHEAT));
        setChestWithItem(helper, POS.offset(2, 1, 3), new ItemStack(Items.WHEAT));
        setChestWithItem(helper, POS.offset(1, 1, 3), new ItemStack(Items.WHEAT));

        // Set output chest
        helper.setBlock(POS.offset(2, 2, 1), Blocks.CHEST);

        // Activate crafter
        helper.setBlock(POS.offset(1, 2, 2), Blocks.REDSTONE_BLOCK);

        helper.succeedWhen(() -> {
            // Result
            assertChestContains(helper, POS.offset(2, 2, 1), new ItemStack(Blocks.CAKE));

            // Inputs must be consumed
            assertChestContains(helper, POS.offset(3, 3, 3), new ItemStack(Items.BUCKET));
            assertChestContains(helper, POS.offset(2, 3, 3), new ItemStack(Items.BUCKET));
            assertChestContains(helper, POS.offset(1, 3, 3), new ItemStack(Items.BUCKET));
            assertChestEmpty(helper, POS.offset(3, 2, 3));
            assertChestEmpty(helper, POS.offset(2, 2, 3));
            assertChestEmpty(helper, POS.offset(1, 2, 3));
            assertChestEmpty(helper, POS.offset(3, 1, 3));
            assertChestEmpty(helper, POS.offset(2, 1, 3));
            assertChestEmpty(helper, POS.offset(1, 1, 3));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testCraftFromChestsToChestBannerCopy1(GameTestHelper helper) {
        helper.setBlock(POS.offset(2, 2, 2), RegistryEntries.BLOCK_STRUCTURED_CRAFTER.value()
                .defaultBlockState()
                .setValue(BlockStructuredCrafter.FACING, Direction.NORTH));

        ItemStack patternBanner = new ItemStack(Items.WHITE_BANNER);
        patternBanner.set(DataComponents.BANNER_PATTERNS, new BannerPatternLayers.Builder()
                .add(helper.getLevel().registryAccess().lookupOrThrow(Registries.BANNER_PATTERN).get(BannerPatterns.FLOWER).get(), DyeColor.YELLOW)
                .build());

        // Define inputs
        setChestWithItem(helper, POS.offset(3, 3, 3), new ItemStack(Items.WHITE_BANNER));
        setChestWithItem(helper, POS.offset(2, 3, 3), patternBanner.copy());
        setChestWithItem(helper, POS.offset(1, 3, 3), ItemStack.EMPTY);
        setChestWithItem(helper, POS.offset(3, 2, 3), ItemStack.EMPTY);
        setChestWithItem(helper, POS.offset(2, 2, 3), ItemStack.EMPTY);
        setChestWithItem(helper, POS.offset(1, 2, 3), ItemStack.EMPTY);
        setChestWithItem(helper, POS.offset(3, 1, 3), ItemStack.EMPTY);
        setChestWithItem(helper, POS.offset(2, 1, 3), ItemStack.EMPTY);
        setChestWithItem(helper, POS.offset(1, 1, 3), ItemStack.EMPTY);

        // Set output chest
        helper.setBlock(POS.offset(2, 2, 1), Blocks.CHEST);

        // Activate crafter
        helper.setBlock(POS.offset(1, 2, 2), Blocks.REDSTONE_BLOCK);

        helper.succeedWhen(() -> {
            // Result
            assertChestContains(helper, POS.offset(2, 2, 1), patternBanner);

            // Inputs must be consumed
            assertChestEmpty(helper, POS.offset(3, 3, 3));
            assertChestContains(helper, POS.offset(2, 3, 3), patternBanner);
            assertChestEmpty(helper, POS.offset(1, 3, 3));
            assertChestEmpty(helper, POS.offset(3, 2, 3));
            assertChestEmpty(helper, POS.offset(2, 2, 3));
            assertChestEmpty(helper, POS.offset(1, 2, 3));
            assertChestEmpty(helper, POS.offset(3, 1, 3));
            assertChestEmpty(helper, POS.offset(2, 1, 3));
            assertChestEmpty(helper, POS.offset(1, 1, 3));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testCraftFromChestsToChestBannerCopy2(GameTestHelper helper) {
        helper.setBlock(POS.offset(2, 2, 2), RegistryEntries.BLOCK_STRUCTURED_CRAFTER.value()
                .defaultBlockState()
                .setValue(BlockStructuredCrafter.FACING, Direction.NORTH));

        ItemStack patternBanner = new ItemStack(Items.WHITE_BANNER);
        patternBanner.set(DataComponents.BANNER_PATTERNS, new BannerPatternLayers.Builder()
                .add(helper.getLevel().registryAccess().lookupOrThrow(Registries.BANNER_PATTERN).get(BannerPatterns.FLOWER).get(), DyeColor.YELLOW)
                .build());

        // Define inputs
        setChestWithItem(helper, POS.offset(3, 3, 3), ItemStack.EMPTY);
        setChestWithItem(helper, POS.offset(2, 3, 3), ItemStack.EMPTY);
        setChestWithItem(helper, POS.offset(1, 3, 3), ItemStack.EMPTY);
        setChestWithItem(helper, POS.offset(3, 2, 3), new ItemStack(Items.WHITE_BANNER));
        setChestWithItem(helper, POS.offset(2, 2, 3), patternBanner.copy());
        setChestWithItem(helper, POS.offset(1, 2, 3), ItemStack.EMPTY);
        setChestWithItem(helper, POS.offset(3, 1, 3), ItemStack.EMPTY);
        setChestWithItem(helper, POS.offset(2, 1, 3), ItemStack.EMPTY);
        setChestWithItem(helper, POS.offset(1, 1, 3), ItemStack.EMPTY);

        // Set output chest
        helper.setBlock(POS.offset(2, 2, 1), Blocks.CHEST);

        // Activate crafter
        helper.setBlock(POS.offset(1, 2, 2), Blocks.REDSTONE_BLOCK);

        helper.succeedWhen(() -> {
            // Result
            assertChestContains(helper, POS.offset(2, 2, 1), patternBanner);

            // Inputs must be consumed
            assertChestEmpty(helper, POS.offset(3, 3, 3));
            assertChestEmpty(helper, POS.offset(2, 3, 3));
            assertChestEmpty(helper, POS.offset(1, 3, 3));
            assertChestEmpty(helper, POS.offset(3, 2, 3));
            assertChestContains(helper, POS.offset(2, 2, 3), patternBanner);
            assertChestEmpty(helper, POS.offset(1, 2, 3));
            assertChestEmpty(helper, POS.offset(3, 1, 3));
            assertChestEmpty(helper, POS.offset(2, 1, 3));
            assertChestEmpty(helper, POS.offset(1, 1, 3));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testCraftFromChestsToChestBannerCopy3(GameTestHelper helper) {
        helper.setBlock(POS.offset(2, 2, 2), RegistryEntries.BLOCK_STRUCTURED_CRAFTER.value()
                .defaultBlockState()
                .setValue(BlockStructuredCrafter.FACING, Direction.NORTH));

        ItemStack patternBanner = new ItemStack(Items.WHITE_BANNER);
        patternBanner.set(DataComponents.BANNER_PATTERNS, new BannerPatternLayers.Builder()
                .add(helper.getLevel().registryAccess().lookupOrThrow(Registries.BANNER_PATTERN).get(BannerPatterns.FLOWER).get(), DyeColor.YELLOW)
                .build());

        // Define inputs
        setChestWithItem(helper, POS.offset(3, 3, 3), ItemStack.EMPTY);
        setChestWithItem(helper, POS.offset(2, 3, 3), ItemStack.EMPTY);
        setChestWithItem(helper, POS.offset(1, 3, 3), ItemStack.EMPTY);
        setChestWithItem(helper, POS.offset(3, 2, 3), ItemStack.EMPTY);
        setChestWithItem(helper, POS.offset(2, 2, 3), ItemStack.EMPTY);
        setChestWithItem(helper, POS.offset(1, 2, 3), ItemStack.EMPTY);
        setChestWithItem(helper, POS.offset(3, 1, 3), new ItemStack(Items.WHITE_BANNER));
        setChestWithItem(helper, POS.offset(2, 1, 3), patternBanner.copy());
        setChestWithItem(helper, POS.offset(1, 1, 3), ItemStack.EMPTY);

        // Set output chest
        helper.setBlock(POS.offset(2, 2, 1), Blocks.CHEST);

        // Activate crafter
        helper.setBlock(POS.offset(1, 2, 2), Blocks.REDSTONE_BLOCK);

        helper.succeedWhen(() -> {
            // Result
            assertChestContains(helper, POS.offset(2, 2, 1), patternBanner);

            // Inputs must be consumed
            assertChestEmpty(helper, POS.offset(3, 3, 3));
            assertChestEmpty(helper, POS.offset(2, 3, 3));
            assertChestEmpty(helper, POS.offset(1, 3, 3));
            assertChestEmpty(helper, POS.offset(3, 2, 3));
            assertChestEmpty(helper, POS.offset(2, 2, 3));
            assertChestEmpty(helper, POS.offset(1, 2, 3));
            assertChestEmpty(helper, POS.offset(3, 1, 3));
            assertChestContains(helper, POS.offset(2, 1, 3), patternBanner);
            assertChestEmpty(helper, POS.offset(1, 1, 3));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testCraftFromChestsToChestBannerCopy4(GameTestHelper helper) {
        helper.setBlock(POS.offset(2, 2, 2), RegistryEntries.BLOCK_STRUCTURED_CRAFTER.value()
                .defaultBlockState()
                .setValue(BlockStructuredCrafter.FACING, Direction.NORTH));

        ItemStack patternBanner = new ItemStack(Items.WHITE_BANNER);
        patternBanner.set(DataComponents.BANNER_PATTERNS, new BannerPatternLayers.Builder()
                .add(helper.getLevel().registryAccess().lookupOrThrow(Registries.BANNER_PATTERN).get(BannerPatterns.FLOWER).get(), DyeColor.YELLOW)
                .build());

        // Define inputs
        setChestWithItem(helper, POS.offset(3, 3, 3), ItemStack.EMPTY);
        setChestWithItem(helper, POS.offset(2, 3, 3), ItemStack.EMPTY);
        setChestWithItem(helper, POS.offset(1, 3, 3), ItemStack.EMPTY);
        setChestWithItem(helper, POS.offset(3, 2, 3), ItemStack.EMPTY);
        setChestWithItem(helper, POS.offset(2, 2, 3), ItemStack.EMPTY);
        setChestWithItem(helper, POS.offset(1, 2, 3), ItemStack.EMPTY);
        setChestWithItem(helper, POS.offset(3, 1, 3), ItemStack.EMPTY);
        setChestWithItem(helper, POS.offset(2, 1, 3), new ItemStack(Items.WHITE_BANNER));
        setChestWithItem(helper, POS.offset(1, 1, 3), patternBanner.copy());

        // Set output chest
        helper.setBlock(POS.offset(2, 2, 1), Blocks.CHEST);

        // Activate crafter
        helper.setBlock(POS.offset(1, 2, 2), Blocks.REDSTONE_BLOCK);

        helper.succeedWhen(() -> {
            // Result
            assertChestContains(helper, POS.offset(2, 2, 1), patternBanner);

            // Inputs must be consumed
            assertChestEmpty(helper, POS.offset(3, 3, 3));
            assertChestEmpty(helper, POS.offset(2, 3, 3));
            assertChestEmpty(helper, POS.offset(1, 3, 3));
            assertChestEmpty(helper, POS.offset(3, 2, 3));
            assertChestEmpty(helper, POS.offset(2, 2, 3));
            assertChestEmpty(helper, POS.offset(1, 2, 3));
            assertChestEmpty(helper, POS.offset(3, 1, 3));
            assertChestEmpty(helper, POS.offset(2, 1, 3));
            assertChestContains(helper, POS.offset(1, 1, 3), patternBanner);
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testCraftFromChestsToChestBannerCopy5(GameTestHelper helper) {
        helper.setBlock(POS.offset(2, 2, 2), RegistryEntries.BLOCK_STRUCTURED_CRAFTER.value()
                .defaultBlockState()
                .setValue(BlockStructuredCrafter.FACING, Direction.NORTH));

        ItemStack patternBanner = new ItemStack(Items.WHITE_BANNER);
        patternBanner.set(DataComponents.BANNER_PATTERNS, new BannerPatternLayers.Builder()
                .add(helper.getLevel().registryAccess().lookupOrThrow(Registries.BANNER_PATTERN).get(BannerPatterns.FLOWER).get(), DyeColor.YELLOW)
                .build());

        // Define inputs
        setChestWithItem(helper, POS.offset(3, 3, 3), ItemStack.EMPTY);
        setChestWithItem(helper, POS.offset(2, 3, 3), new ItemStack(Items.WHITE_BANNER));
        setChestWithItem(helper, POS.offset(1, 3, 3), ItemStack.EMPTY);
        setChestWithItem(helper, POS.offset(3, 2, 3), ItemStack.EMPTY);
        setChestWithItem(helper, POS.offset(2, 2, 3), ItemStack.EMPTY);
        setChestWithItem(helper, POS.offset(1, 2, 3), ItemStack.EMPTY);
        setChestWithItem(helper, POS.offset(3, 1, 3), ItemStack.EMPTY);
        setChestWithItem(helper, POS.offset(2, 1, 3), patternBanner.copy());
        setChestWithItem(helper, POS.offset(1, 1, 3), ItemStack.EMPTY);

        // Set output chest
        helper.setBlock(POS.offset(2, 2, 1), Blocks.CHEST);

        // Activate crafter
        helper.setBlock(POS.offset(1, 2, 2), Blocks.REDSTONE_BLOCK);

        helper.succeedWhen(() -> {
            // Result
            assertChestContains(helper, POS.offset(2, 2, 1), patternBanner);

            // Inputs must be consumed
            assertChestEmpty(helper, POS.offset(3, 3, 3));
            assertChestEmpty(helper, POS.offset(2, 3, 3));
            assertChestEmpty(helper, POS.offset(1, 3, 3));
            assertChestEmpty(helper, POS.offset(3, 2, 3));
            assertChestEmpty(helper, POS.offset(2, 2, 3));
            assertChestEmpty(helper, POS.offset(1, 2, 3));
            assertChestEmpty(helper, POS.offset(3, 1, 3));
            assertChestContains(helper, POS.offset(2, 1, 3), patternBanner);
            assertChestEmpty(helper, POS.offset(1, 1, 3));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testCraftFromChestsToChestBannerCopy6(GameTestHelper helper) {
        helper.setBlock(POS.offset(2, 2, 2), RegistryEntries.BLOCK_STRUCTURED_CRAFTER.value()
                .defaultBlockState()
                .setValue(BlockStructuredCrafter.FACING, Direction.NORTH));

        ItemStack patternBanner = new ItemStack(Items.WHITE_BANNER);
        patternBanner.set(DataComponents.BANNER_PATTERNS, new BannerPatternLayers.Builder()
                .add(helper.getLevel().registryAccess().lookupOrThrow(Registries.BANNER_PATTERN).get(BannerPatterns.FLOWER).get(), DyeColor.YELLOW)
                .build());

        // Define inputs
        setChestWithItem(helper, POS.offset(3, 3, 3), ItemStack.EMPTY);
        setChestWithItem(helper, POS.offset(2, 3, 3), patternBanner.copy());
        setChestWithItem(helper, POS.offset(1, 3, 3), ItemStack.EMPTY);
        setChestWithItem(helper, POS.offset(3, 2, 3), ItemStack.EMPTY);
        setChestWithItem(helper, POS.offset(2, 2, 3), ItemStack.EMPTY);
        setChestWithItem(helper, POS.offset(1, 2, 3), ItemStack.EMPTY);
        setChestWithItem(helper, POS.offset(3, 1, 3), ItemStack.EMPTY);
        setChestWithItem(helper, POS.offset(2, 1, 3), new ItemStack(Items.WHITE_BANNER));
        setChestWithItem(helper, POS.offset(1, 1, 3), ItemStack.EMPTY);

        // Set output chest
        helper.setBlock(POS.offset(2, 2, 1), Blocks.CHEST);

        // Activate crafter
        helper.setBlock(POS.offset(1, 2, 2), Blocks.REDSTONE_BLOCK);

        helper.succeedWhen(() -> {
            // Result
            assertChestContains(helper, POS.offset(2, 2, 1), patternBanner);

            // Inputs must be consumed
            assertChestEmpty(helper, POS.offset(3, 3, 3));
            assertChestContains(helper, POS.offset(2, 3, 3), patternBanner);
            assertChestEmpty(helper, POS.offset(1, 3, 3));
            assertChestEmpty(helper, POS.offset(3, 2, 3));
            assertChestEmpty(helper, POS.offset(2, 2, 3));
            assertChestEmpty(helper, POS.offset(1, 2, 3));
            assertChestEmpty(helper, POS.offset(3, 1, 3));
            assertChestEmpty(helper, POS.offset(2, 1, 3));
            assertChestEmpty(helper, POS.offset(1, 1, 3));
        });
    }

    protected void setChestWithItem(GameTestHelper helper, BlockPos pos, ItemStack itemStack) {
        helper.setBlock(pos, Blocks.CHEST);
        ChestBlockEntity chest = helper.getBlockEntity(pos);
        chest.setItem(0, itemStack);
    }

    protected void assertChestEmpty(GameTestHelper helper, BlockPos pos) {
        helper.assertBlockEntityData(pos, (ChestBlockEntity chest) -> chest.isEmpty(), () -> "Chest is not empty");
    }

    protected void assertChestContains(GameTestHelper helper, BlockPos pos, ItemStack itemStack) {
        helper.assertBlockEntityData(pos, (ChestBlockEntity chest) -> ItemStack.isSameItemSameComponents(chest.getItem(0), itemStack), () -> "Chest is not empty");
    }

}
