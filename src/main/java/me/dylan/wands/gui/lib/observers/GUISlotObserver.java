package me.dylan.wands.gui.lib.observers;

import me.dylan.wands.gui.lib.views.GUIPageView;
import org.bukkit.inventory.ItemStack;

public interface GUISlotObserver {
    boolean onChange(GUIPageView view, ItemStack from, ItemStack to);
}
