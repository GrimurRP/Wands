package me.dylan.wands.gui.lib;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface GUIWindowProvider {
    Inventory createWindow(Player player);
}
