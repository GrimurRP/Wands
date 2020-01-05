package me.dylan.wands.gui.lib.regions;

import me.dylan.wands.gui.lib.GUISlot;
import me.dylan.wands.gui.lib.building.GUIDynamicButton;
import me.dylan.wands.gui.lib.observers.GUIObserverMap;
import me.dylan.wands.gui.lib.observers.GUISlotObserver;
import me.dylan.wands.gui.lib.regions.dynamic.GUIDynamicRegion;
import me.dylan.wands.gui.lib.views.GUIRegionView;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class GUIRootRegion extends GUIRegion {
    private final GUIRegionType regionType;

    private GUIRegionMap regionMap;
    private GUIObserverMap observerMap;

    public GUIRootRegion(GUIRegionType type, int width, int height) {
        super(null, 0, 0, width, height);
        this.regionType = type;
    }

    public GUISlot getSlot(int slot) {
        return new GUISlot(this, slot);
    }

    public GUISlot getSlot(int x, int y) {
        return new GUISlot(this, x, y);
    }

    @Override
    public int getGUISlot(int x, int y) {
        return regionType.getFirstSlot() + (x + y * width);
    }

    public GUIRegion getRegionAtSlot(int slot) {
        return (regionMap == null) ? null : regionMap.getRegionFromSlot(slot - regionType.getFirstSlot());
    }

    @Override
    public GUIRegion getRegionAt(int x, int y) {
        return (regionMap == null) ? this : regionMap.getRegionAt(x, y);
    }

    @Override
    public GUIRegion addRegion(int x, int y, int width, int height) {
        GUIRegion subRegion = GUIRegion.createRegion(this, x, y, width, height);
        this.getOrCreateRegionMap().addRegion(subRegion);
        return subRegion;
    }

    @Override
    public GUIDynamicRegion addDynamicRegion(int x, int y, int width, int height) {
        GUIDynamicRegion subRegion = new GUIDynamicRegion(this, x, y, width, height);
        this.getOrCreateRegionMap().addRegion(subRegion);
        return subRegion;
    }

    public GUIRootRegion setObserver(int slot, GUISlotObserver observer) {
        getOrCreateObserverMap().setSlotObserver(slot - regionType.getFirstSlot(), observer);
        return this;
    }

    public GUIRootRegion setObserver(int x, int y, GUISlotObserver observer) {
        getOrCreateObserverMap().setSlotObserver(x + y * width, observer);
        return this;
    }

    public boolean notifyObservers(int x, int y, ItemStack from, ItemStack to, GUIRegionView view) {
        return notifyObservers(getGUISlot(x, y), from, to, view);
    }

    public boolean notifyObservers(int slot, ItemStack from, ItemStack to, GUIRegionView view) {
        if (observerMap == null)
            return false;

        return observerMap.notifyObservers(slot, from, to, view);
    }

    @Override
    public void getItems(BiConsumer<Integer, ItemStack> consumer) {
        if (regionMap == null)
            return;

        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                GUIRegion region = regionMap.getRegionAt(i, j);

                int slot = getGUISlot(i, j);
                ItemStack item = region.getItem();

                consumer.accept(slot, item);
            }
        }
    }

    public void getDynamicContents(GUIRegionView view, Player player, BiConsumer<Integer, GUIDynamicButton> consumer) {
        if (regionMap == null)
            return;

        List<GUIDynamicButton> allButtons = new ArrayList<>();
        for (GUIDynamicRegion region : regionMap.viewDynamicRegions()) {
            List<GUIDynamicButton> buttons = region.getContentProvider().getContents(view, player);

            allButtons.addAll(buttons);

            int row = 0, column = 0;
            for (GUIDynamicButton button : buttons) {
                int slot = region.getGUISlot(column, row);
                consumer.accept(slot, button);

                column += 1;
                if (column > region.getWidth()) {
                    column = 0;
                    row += 1;
                }
            }
            // TODO PAGINATION
        }
    }

    @Override
    public GUIRootRegion getRootRegion() {
        return this;
    }

    public GUIRegionType getRegionType() {
        return this.regionType;
    }

    private GUIRegionMap getOrCreateRegionMap() {
        if (regionMap == null) {
            regionMap = new GUIRegionMap(width, height);
            regionMap.addRegion(this);
        }

        return regionMap;
    }

    private GUIObserverMap getOrCreateObserverMap() {
        if (observerMap == null)
            observerMap = new GUIObserverMap(width, height);

        return observerMap;
    }

    public static GUIRootRegion makeResizedShallowCopy(GUIRootRegion old, int newWidth, int newHeight) {
        GUIRootRegion newRegion = new GUIRootRegion(old.regionType, newWidth, newHeight);

        if (old.regionMap != null) {
            GUIRegionMap newRegionMap = newRegion.getOrCreateRegionMap();
            for (GUIRegion region : old.regionMap) {
                newRegionMap.addRegion(region);
            }
        }

        return newRegion;
    }

}
