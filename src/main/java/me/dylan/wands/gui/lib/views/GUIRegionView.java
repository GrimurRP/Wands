package me.dylan.wands.gui.lib.views;

import me.dylan.wands.gui.lib.regions.GUIRegion;
import me.dylan.wands.gui.lib.regions.GUIRegionType;
import me.dylan.wands.gui.lib.regions.GUIRootRegion;

public class GUIRegionView {
    private final GUIPageView page;
    private final GUIRegion region;

    public GUIRegionView(GUIPageView pageView, GUIRegion region) {
        this.page = pageView;
        this.region = region;
    }

    public GUIRegionType getRegionType() {
        GUIRootRegion root = region.getRootRegion();
        return (root == null) ? null : root.getRegionType();
    }

    public GUIPageView getPage() {
        return page;
    }

    public GUISlotView viewSlot(int x, int y) {
        return new GUISlotView(region.getSlot(x, y));
    }

}
