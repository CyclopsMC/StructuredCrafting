package org.cyclops.structuredcrafting.craft;

import com.google.common.base.Objects;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
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
        setInventorySlotContents(col * 3 + row, itemStack.copy());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof WorldInventoryCrafting)) {
            return false;
        }
        for (int i = 0; i < getSizeInventory(); i++) {
            if (!ItemStack.areItemStacksEqual(this.getStackInSlot(i), ((WorldInventoryCrafting) obj).getStackInSlot(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 11 + getSizeInventory();
        for (int i = 0; i < getSizeInventory(); i++) {
            hash = hash << 1;
            hash |= getItemStackHashCode(getStackInSlot(i));
        }
        return hash;
    }

    public static int getItemStackHashCode(ItemStack itemStack) {
        if (itemStack == null) {
            return 0;
        }
        return Objects.hashCode(itemStack.getCount(), itemStack.getMetadata(),
                Item.getIdFromItem(itemStack.getItem()), itemStack.hasTagCompound() ? itemStack.getTagCompound() : 0);
    }
}
