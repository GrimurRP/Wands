package me.dylan.wands.gui.lib.building;

import me.dylan.wands.gui.lib.GUI;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.Plugin;

public class GUIBuilder {

    public GUIBuilder() {}

    public GUIChestPageBuilder newChestPage(String name) {
        return new GUIChestPageBuilder(name);
    }

    public GUIPageBuilder newPage(String name, InventoryType type) {
        return new GUIPageBuilder(name, type);
    }

    public GUI build(GUIChestPageBuilder rootPage, Plugin plugin) {
        return new GUI(plugin, rootPage.build());
    }

    public GUI build(GUIPageBuilder rootPage, Plugin plugin) {
        return new GUI(plugin, rootPage.build());
    }

}
