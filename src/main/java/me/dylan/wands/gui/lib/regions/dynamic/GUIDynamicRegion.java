package me.dylan.wands.gui.lib.regions.dynamic;

import me.dylan.wands.gui.lib.regions.GUIRegion;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;

public class GUIDynamicRegion extends GUIRegion {
    private GUIContentProvider contentProvider;
    private boolean isPaginated;

    public GUIDynamicRegion(GUIRegion parent, int x, int y, int width, int height) {
        super(parent, x, y, width, height);
    }

    public GUIContentProvider getContentProvider() {
        return this.contentProvider;
    }

    public GUIRegion setContentProvider(GUIContentProvider contentSupplier) {
        this.contentProvider = contentSupplier;
        return this;
    }

    public GUIRegion setPaginated(boolean isPaginated) {
        if (isPaginated) {
            if (width < 2)
                throw new UnsupportedOperationException("Region width must be at least 2 to support pagination!");
            if (height < 2)
                throw new UnsupportedOperationException("Region height must be at least 2 to support pagination!");

            this.isPaginated = true;
        } else {
            this.isPaginated = false;
        }
        return this;
    }

    public boolean isPaginated() {
        return this.isPaginated;
    }

    @Override
    public GUIRegion addRegion(int x, int y, int width, int height) {
        throw new UnsupportedOperationException("New region can't overlap a dynamic region");
    }

    @Override
    public void getItems(BiConsumer<Integer, ItemStack> consumer) {
    }
}
