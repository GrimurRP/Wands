package me.dylan.wands.gui.lib;

import me.dylan.wands.gui.lib.actions.GUIClickAction;
import me.dylan.wands.gui.lib.observers.GUISlotObserver;
import me.dylan.wands.gui.lib.regions.GUIRegion;
import me.dylan.wands.gui.lib.regions.GUIRootRegion;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

public class GUISlot {
    private final GUIRootRegion region;
    private final int x, y;

    public GUISlot(GUIRootRegion region, int x, int y) {
        this.region = region;
        this.x = x;
        this.y = y;
    }

    public GUISlot(GUIRootRegion region, int slot) {
        this.region = region;

        slot -= region.getRegionType().getFirstSlot();

        this.x = slot / region.getWidth();
        this.y = slot % region.getHeight();
    }

    public GUISlot setObserver(GUISlotObserver observer) {
        region.setObserver(x, y, observer);
        return this;
    }

    public GUISlot setClickAction(GUIClickAction action)  {
        getSingleSlotRegion(true).setAction(action);
        return this;
    }

    public GUISlot setProtected(boolean isProtected) {
        getSingleSlotRegion(true).setProtected(isProtected);
        return this;
    }

    public boolean isProtected() {
        return this.region.getRegionAt(x, y).isProtected();
    }

    public GUISlot setItem(ItemStack item) {
        getSingleSlotRegion(true).setItem(item);
        return this;
    }

    public ItemStack getItem() {
        return getSingleSlotRegion(false).getItem();
    }

    private GUIRegion getSingleSlotRegion(boolean create) {
        GUIRegion region = this.region.getRegionAt(x, y);
        if (create && region.getWidth() != 1 || region.getHeight() != 1) {
            region = this.region.addRegion(x, y, 1, 1);
        }
        return region;
    }

}
