package me.dylan.wands.gui.lib.regions;

import me.dylan.wands.gui.lib.regions.dynamic.GUIDynamicRegion;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

public class GUIRegionMap implements Iterable<GUIRegion> {
    private static final class RefCountedRegion {
        private static int lastID = 0;

        private final GUIRegion region;
        private int refCount;

        private final int id;

        public RefCountedRegion(GUIRegion region) {
            this.region = region;
            this.refCount = region.width * region.height;

            this.id = lastID++;
        }
    }

    private final RefCountedRegion[] regionMap;
    private final int width, height;

    private List<GUIDynamicRegion> dynamicRegions;

    // Preserving insertion order is important - regions that were added last must be on top!
    private LinkedHashMap<Integer, GUIRegion> uniqueRegions;

    public GUIRegionMap(int width, int height) {
        this.regionMap = new RefCountedRegion[width * height];
        this.width = width;
        this.height = height;
    }

    public void addRegion(GUIRegion region) {
        int x1 = region.getX(), x2 = x1 + region.getWidth();
        int y1 = region.getY(), y2 = y1 + region.getHeight();

        if (x1 == x2 && y1 == y2)
            return;

        checkInBounds(x1, y1);
        checkInBounds(x2, y2);

        RefCountedRegion wrapper = new RefCountedRegion(region);

        Map<Integer, GUIRegion> uniqueRegions = getUniqueRegionMap();
        uniqueRegions.put(wrapper.id, region);

        if (region instanceof GUIDynamicRegion)
            getDynamicRegions().add((GUIDynamicRegion) region);

        for (int x = x1; x < x2; ++x) {
            for (int y = y1; y < y2; ++y) {
                int index = x + y * width;
                RefCountedRegion old = regionMap[index];
                if (old != null) {
                    if (old.region instanceof GUIDynamicRegion)
                        throw new IllegalStateException("New region can't overlap a dynamic region");
                    if (--old.refCount == 0)
                        uniqueRegions.remove(old.id);
                }

                regionMap[index] = wrapper;
            }
        }
    }

    public GUIRegion getRegionFromSlot(int slot) {
        int x = slot % width;
        int y = slot / width;
        return getRegionAt(x, y);
    }

    public GUIRegion getRegionAt(int x, int y) {
        checkInBounds(x, y);

        RefCountedRegion wrapper = regionMap[x + y * width];
        return (wrapper == null) ? null : wrapper.region;
    }

    public List<GUIDynamicRegion> viewDynamicRegions() {
        return (dynamicRegions == null) ? Collections.emptyList() : Collections.unmodifiableList(dynamicRegions);
    }

    private void checkInBounds(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height)
            throw new IllegalArgumentException("Region out of bounds");
    }

    private Map<Integer, GUIRegion> getUniqueRegionMap() {
        if (uniqueRegions == null)
            uniqueRegions = new LinkedHashMap<>();

        return uniqueRegions;
    }

    private List<GUIDynamicRegion> getDynamicRegions() {
        if (dynamicRegions == null)
            dynamicRegions = new ArrayList<>();

        return dynamicRegions;
    }

    @NotNull
    @Override
    public Iterator<GUIRegion> iterator() {
        if (uniqueRegions == null)
            return Collections.emptyIterator();
        return Collections.unmodifiableCollection(uniqueRegions.values()).iterator();
    }

    @Override
    public void forEach(Consumer<? super GUIRegion> action) {
        if (uniqueRegions == null)
            return;
        uniqueRegions.values().forEach(action);
    }

}
