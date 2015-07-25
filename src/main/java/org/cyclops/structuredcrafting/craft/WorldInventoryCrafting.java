package org.cyclops.structuredcrafting.craft;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

/**
 * A world-based implementation of the crafting container.
 * @author rubensworks
 */
public class WorldInventoryCrafting extends InventoryCrafting {

    public WorldInventoryCrafting() {
        super(new Container() {
            @Override
            public boolean canInteractWith(EntityPlayer playerIn) {
                return false;
            }
        }, 3, 3);
    }

    public void setItemStack(int row, int col, ItemStack itemStack) {
        setInventorySlotContents(col * 3 + row, itemStack);
    }

}
