package me.dylan.wands.gui.lib.regions;

import me.dylan.wands.gui.lib.GUISlot;
import me.dylan.wands.gui.lib.actions.GUIClickAction;
import me.dylan.wands.gui.lib.regions.dynamic.GUIDynamicRegion;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;

public class GUIRegion {
    private GUIRegion parent;

    private boolean isProtected;

    private int x, y;
    protected int width, height;

    private GUIClickAction clickListener;

    private ItemStack item;

    protected GUIRegion(GUIRegion parent, int x, int y, int width, int height) {
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        if (parent != null) { // Inherit default values
            this.setProtected(parent.isProtected());
            this.setItem(parent.getItem());
        }
    }

    int getX() { return x; }

    int getY() { return y; }

    public int getWidth() { return width;}

    public int getHeight() { return height; }

    public int getGUISlot(int x, int y) {
        return parent.getGUISlot(this.x + x, this.y + y);
    }

    public GUISlot getSlot(int x, int y) {
        return parent.getSlot(this.x + x, this.y + y);
    }

    public GUIRootRegion getRootRegion() {
        return (parent == null) ? null : parent.getRootRegion();
    }

    public boolean containsSlot(int x, int y) {
        return parent.containsSlot(this.x + x, this.y + y);
    }

    public GUIRegion setProtected(boolean isProtected) {
        this.isProtected = isProtected;
        return this;
    }

    public boolean isProtected() {
        return this.isProtected;
    }

    public GUIRegion setItem(ItemStack item) {
        this.item = item;
        return this;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public GUIRegion setAction(GUIClickAction action) {
        this.clickListener = action;
        return this;
    }

    public GUIClickAction getAction() {
        return this.clickListener;
    }

    public GUIRegion getRegionAt(int x, int y) {
        return parent.getRegionAt(this.x + x, this.y + y);
    }

    public GUIRegion addRegion(int x, int y, int width, int height) {
        return parent.addRegion(this.x + x, this.y + y, width, height);
    }

    public GUIDynamicRegion addDynamicRegion(int x, int y, int width, int height) {
        return parent.addDynamicRegion(x, y, width, height);
    }

    public void getItems(BiConsumer<Integer, ItemStack> consumer) {
        for (int i = 0; i < width; ++i) {
            for (int j = 0; j < height; ++j) {
                GUIRegion region = getRegionAt(i, j);

                int slot = getGUISlot(i, j);
                ItemStack item = region.getItem();

                consumer.accept(slot, item);
            }
        }
    }

    public int getRootX(int localX) {
        return (parent == null) ? localX : parent.getRootX(this.x + localX);
    }

    public int getRootY(int localY) {
        return (parent == null) ? localY : parent.getRootY(this.y + localY);
    }

    @Override
    public String toString() {
        return "GUIRegion{ x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + ", hasParent=" + (parent != null) + ", protected=" + isProtected + "}";
    }

    public static GUIRegion createRegion(GUIRegion parent, int x, int y, int width, int height) {
        if (x == 0 && y == 0 && width == parent.width && height == parent.height)
            return parent;

        return new GUIRegion(parent, x, y, width, height);
    }

    public static GUIRootRegion createChestRoot(int rows) {
        return new GUIRootRegion(GUIRegionType.CHEST_CONTENTS, 9, rows);
    }

    public static GUIRootRegion createRoot(GUIRegionType regionType) {
        return new GUIRootRegion(regionType, regionType.getRegionWidth(), regionType.getRegionHeight());
    }

}
