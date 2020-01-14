package me.dylan.wands.gui.lib.regions;

import me.dylan.wands.gui.lib.GUISlot;
import me.dylan.wands.gui.lib.actions.GUIClickAction;
import me.dylan.wands.gui.lib.building.GUIDynamicButton;
import me.dylan.wands.gui.lib.observers.GUIObserverMap;
import me.dylan.wands.gui.lib.observers.GUISlotObserver;
import me.dylan.wands.gui.lib.regions.dynamic.GUIDynamicRegion;
import me.dylan.wands.gui.lib.views.GUIRegionView;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
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

    @Override
    public GUISlot getSlot(int x, int y) {
        return new GUISlot(this, x, y);
    }

    @Override
    public int getGUISlot(int x, int y) {
        return regionType.getFirstSlot() + (x + y * width);
    }

    @Override
    public boolean containsSlot(int x, int y) {
        return x >= 0 && y >= 0 && x < width && y < height;
    }

    public boolean containsSlot(int slot) {
        int unmapped = slot - this.regionType.getFirstSlot();
        return unmapped >= 0 && unmapped < width*height;
    }

    @Override
    public GUIRootRegion setProtected(boolean isProtected) {
        super.setProtected(isProtected);
        return this;
    }

    @Override
    public GUIRootRegion setItem(ItemStack item) {
        super.setItem(item);
        return this;
    }

    @Override
    public GUIRootRegion setAction(GUIClickAction action) {
        super.setAction(action);
        return this;
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

    public Collection<GUIRegion> viewAllRegions() {
        return Collections.unmodifiableCollection(regionMap.viewAllRegions());
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

            Set<Integer> visited = new HashSet<>();

            int maxItems = region.width * region.height;

            int row = 0, column = 0;
            int items = 0;
            for (GUIDynamicButton button : buttons) {
                int slot = region.getGUISlot(column, row);
                consumer.accept(slot, button);

                visited.add(slot);

                if (++items == maxItems)
                    break;

                column += 1;
                if (column >= region.getWidth()) {
                    column = 0;
                    row += 1;
                }
            }
            // Fill the rest with empty items

            GUIDynamicButton nullButton = new GUIDynamicButton(region.getItem(), null);

            int first = region.getGUISlot(0, 0);
            for (int i = 0; i < region.width; ++i) {
                for (int j = 0; j < region.height; ++j) {
                    int slot = first + (i + j * this.width);
                    if (visited.contains(slot))
                        continue;

                    consumer.accept(slot, nullButton);
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

}
