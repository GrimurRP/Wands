package me.dylan.wands.gui.lib.building;

import me.dylan.wands.gui.lib.GUIPage;
import me.dylan.wands.gui.lib.regions.GUIRegion;
import me.dylan.wands.gui.lib.regions.GUIRegionType;
import me.dylan.wands.gui.lib.regions.GUIRootRegion;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;

import java.util.EnumMap;
import java.util.Map;

public class GUIPageBuilder {
    private final InventoryType type;
    private final String name;

    private Map<GUIRegionType, GUIRootRegion> regions;

    GUIPageBuilder(String name, InventoryType type) {
        this.type = type;
        this.name = name;
        this.regions = new EnumMap<>(GUIRegionType.class);
    }

    public GUIRegion addRegion(GUIRegionType slotType, int x, int y, int width, int height) {
        GUIRegion region = getOrCreateRegion(slotType);
        return region.addRegion(x, y, width, height);
    }

    public GUIRootRegion getOrCreateRegion(GUIRegionType slotType) {
        return regions.computeIfAbsent(slotType, k -> GUIRegion.createRoot(slotType));
    }

    public GUIPage build() {
        return new GUIPage(this.regions, p -> Bukkit.createInventory(p, type, name));
    }

}
