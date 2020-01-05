package me.dylan.wands.gui.lib.actions;

import me.dylan.wands.gui.lib.views.GUIRegionView;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface GUIClickAction {
    boolean onClick(GUIRegionView regionView, ItemStack clickedItem, Player player);
}
