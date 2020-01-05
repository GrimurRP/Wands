package me.dylan.wands.gui.lib.observers;

import me.dylan.wands.gui.lib.regions.GUIRegion;
import me.dylan.wands.gui.lib.views.GUIRegionView;
import org.bukkit.inventory.ItemStack;

public class GUIObserverMap {
    private static final class Slot {
        GUISlotObserver slotObserver;
        GUIRegionObserver regionObserver;
    }

    private final Slot[] map;

    public GUIObserverMap(int width, int height) {
        this.map = new Slot[width * height];
    }

    public void setSlotObserver(int slotIndex, GUISlotObserver observer) {
        getOrCreateSlot(slotIndex).slotObserver = observer;
    }

    public void setRegionObserver(int x, int y, GUIRegion region, GUIRegionObserver observer) {
        int width = region.getWidth();
        int height = region.getHeight();

    }

    public void setRegionObserver(int slotIndex, GUIRegionObserver observer) {
        getOrCreateSlot(slotIndex).regionObserver = observer;
    }

    private Slot getOrCreateSlot(int index) {
        Slot slot = map[index];
        if (slot == null) {
            slot = new Slot();
            map[index] = slot;
        }
        return slot;
    }

    public boolean notifyObservers(int slotIndex, ItemStack from, ItemStack to, GUIRegionView region) {
        Slot slot = map[slotIndex];
        if (slot == null)
            return false;

        boolean cancel = false;
        if (slot.slotObserver != null)
            cancel |= slot.slotObserver.onChange(region.getPage(), from, to);
        if (slot.regionObserver != null)
            cancel |= slot.regionObserver.onChange(region, from, to, slotIndex);

        return cancel;
    }

}
