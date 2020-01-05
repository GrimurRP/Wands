package me.dylan.wands.gui.lib.observers;

import me.dylan.wands.gui.lib.views.GUIRegionView;
import org.bukkit.inventory.ItemStack;

public interface GUIRegionObserver {
    boolean onChange(GUIRegionView clickedRegion, ItemStack from, ItemStack to, int slot);
}
