package me.dylan.wands.gui.lib;

import me.dylan.wands.gui.lib.actions.GUIClickAction;
import me.dylan.wands.gui.lib.observers.GUISlotObserver;
import me.dylan.wands.gui.lib.regions.GUIRegion;
import me.dylan.wands.gui.lib.regions.GUIRootRegion;
import org.bukkit.inventory.ItemStack;

public class GUISlot {
    private final GUIRootRegion region;
    private final int slot;

    public GUISlot(GUIRootRegion region, int x, int y) {
        this(region, region.getGUISlot(x, y));
    }

    public GUISlot(GUIRootRegion region, int slot) {
        this.region = region;
        this.slot = slot;
    }

    public GUISlot setObserver(GUISlotObserver observer) {
        region.setObserver(slot, observer);
        return this;
    }

    public GUISlot setClickAction(GUIClickAction action)  {
        getSingleSlotRegion().setAction(action);
        return this;
    }

    public GUISlot setProtected(boolean isProtected) {
        getSingleSlotRegion().setProtected(isProtected);
        return this;
    }

    public boolean isProtected() {
        return this.region.getRegionAtSlot(slot).isProtected();
    }

    public GUISlot setItem(ItemStack item) {
        getSingleSlotRegion().setItem(item);
        return this;
    }

    public ItemStack getItem() {
        return getSingleSlotRegion().getItem();
    }

    private GUIRegion getSingleSlotRegion() {
        GUIRegion region = this.region.getRegionAtSlot(slot);
        if (region.getWidth() != 1 || region.getHeight() != 1) {
            int x = slot % region.getWidth();
            int y = slot / region.getHeight();
            region = this.region.createRegion(this.region, x, y, 1, 1);
        }
        return region;
    }

}
