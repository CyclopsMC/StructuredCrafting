package org.cyclops.structuredcrafting.craft;

import com.google.common.base.Objects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * A world-based implementation of the crafting container.
 * @author rubensworks
 */
public class WorldInventoryCrafting extends CraftingInventory {

    public WorldInventoryCrafting() {
        super(new Container(ContainerType.CRAFTING, 0) {
            @Override
            public boolean stillValid(PlayerEntity playerIn) {
                return false;
            }
        }, 3, 3);
    }

    public void setItemStack(int row, int col, ItemStack itemStack) {
        setItem(col * 3 + row, itemStack.copy());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof WorldInventoryCrafting)) {
            return false;
        }
        for (int i = 0; i < getContainerSize(); i++) {
            if (!ItemStack.matches(this.getItem(i), ((WorldInventoryCrafting) obj).getItem(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 11 + getContainerSize();
        for (int i = 0; i < getContainerSize(); i++) {
            hash = hash << 1;
            hash |= getItemStackHashCode(getItem(i));
        }
        return hash;
    }

    public static int getItemStackHashCode(ItemStack itemStack) {
        if (itemStack == null) {
            return 0;
        }
        return Objects.hashCode(itemStack.getCount(), Item.getId(itemStack.getItem()),
                itemStack.hasTag() ? itemStack.getTag() : 0);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.getContainerSize(); i++) {
            sb.append(this.getItem(i));
            sb.append(",");
        }
        return sb.toString();
    }
}
