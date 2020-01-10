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

    private GUIRootRegion region;
    private int maxHeight = -1;

    GUIChestPageBuilder(String name) {
        this.name = name;
        this.region = GUIRegion.createChestRoot(6);
    }

    public GUIChestPageBuilder setPreferredHeight(int height) {
        this.maxHeight = height;
        return this;
    }

    public GUIRegion addRegion(int x, int y, int width, int height) {
        this.maxHeight = Math.max(this.maxHeight, y + height);
        return region.addRegion(x, y, width, height);
    }

    public GUIRootRegion getRootRegion() {
        return region;
    }

    public GUIPage build() {
        GUIRootRegion region = shrinkToFit();
        Map<GUIRegionType, GUIRootRegion> regions = Collections.singletonMap(GUIRegionType.CHEST_CONTENTS, region);

        return new GUIPage(regions, p -> Bukkit.createInventory(p, 9 * region.getHeight(), name));
    }

    private GUIRootRegion shrinkToFit() {
        int rowCount = maxHeight;
        if (rowCount == -1) { // no addRegion() calls done

        }
        if (rowCount > 6) {
            rowCount = 6;
        }

        System.out.println("maxHeight: " + maxHeight + ", rowCount: " + rowCount);

        return region;//GUIRootRegion.makeResizedShallowCopy(region, region.getWidth(), rowCount);
    }

}
