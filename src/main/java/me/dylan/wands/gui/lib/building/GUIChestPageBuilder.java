package me.dylan.wands.gui.lib.building;

import me.dylan.wands.gui.lib.GUIPage;
import me.dylan.wands.gui.lib.regions.GUIRegion;
import me.dylan.wands.gui.lib.regions.GUIRootRegion;

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
        return null;
    }

    private GUIRootRegion shrinkToFit() {
        int rowCount = maxHeight;
        if (rowCount == -1) { // no addRegion() calls done
            rowCount = 3;
        }

        return GUIRootRegion.makeResizedShallowCopy(region, region.getWidth(), rowCount);
    }

}
