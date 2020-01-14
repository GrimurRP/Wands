package me.dylan.wands.gui.lib.building;

import me.dylan.wands.gui.lib.GUIPage;
import me.dylan.wands.gui.lib.regions.GUIRegion;
import me.dylan.wands.gui.lib.regions.GUIRegionType;
import me.dylan.wands.gui.lib.regions.GUIRootRegion;
import org.bukkit.Bukkit;

import java.util.Collections;
import java.util.Map;

public class GUIChestPageBuilder {
    private final String name;
    private final GUIRootRegion region;

    GUIChestPageBuilder(String name, int height) {
        this.name = name;
        this.region = GUIRegion.createChestRoot(height);
    }

    public GUIRegion addRegion(int x, int y, int width, int height) {
        return region.addRegion(x, y, width, height);
    }

    public GUIRootRegion getRootRegion() {
        return region;
    }

    public GUIPage build() {
        Map<GUIRegionType, GUIRootRegion> regions = Collections.singletonMap(GUIRegionType.CHEST_CONTENTS, region);
        return new GUIPage(regions, p -> Bukkit.createInventory(p, 9 * region.getHeight(), name));
    }


}
