package org.cyclops.structuredcrafting.craft;

import com.google.common.base.Objects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * A world-based implementation of the crafting container.
 * @author rubensworks
 */
public class WorldInventoryCrafting extends TransientCraftingContainer {

    public WorldInventoryCrafting() {
        super(new AbstractContainerMenu(MenuType.CRAFTING, 0) {
            @Override
            public ItemStack quickMoveStack(Player p_38941_, int p_38942_) {
                return ItemStack.EMPTY;
            }

            @Override
            public boolean stillValid(Player playerIn) {
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
