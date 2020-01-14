package me.dylan.wands.gui.lib.building;

import me.dylan.wands.gui.lib.GUI;
import me.dylan.wands.gui.lib.GUIPage;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.Plugin;

public class GUIBuilder {

    public GUIBuilder() {}

    public GUIChestPageBuilder newChestPage(String name, int rows) {
        return new GUIChestPageBuilder(name, rows);
    }

    public GUIPageBuilder newPage(String name, InventoryType type) {
        return new GUIPageBuilder(name, type);
    }

    public GUI build(GUIPage rootPage, Plugin plugin) {
        return new GUI(plugin, rootPage);
    }

}
