package me.dylan.wands.gui.lib.views;

import me.dylan.wands.WandsPlugin;
import me.dylan.wands.gui.lib.GUIPage;
import me.dylan.wands.gui.lib.GUIState;
import me.dylan.wands.gui.lib.ItemPickupProtection;
import me.dylan.wands.gui.lib.actions.GUIClickAction;
import me.dylan.wands.gui.lib.regions.GUIRegion;
import me.dylan.wands.gui.lib.regions.GUIRegionType;
import me.dylan.wands.gui.lib.regions.GUIRootRegion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

// A per-player page view handler
public class GUIPageView {
    private final GUIPage page;
    private Map<GUIRegionType, GUIRootRegion> regions;

    private final Player player;

    private GUIState stateTracker;
    private Map<GUIRegionType, Map<Integer, GUIClickAction>> dynamicButtons;

    private Inventory inventory;

    public GUIPageView(GUIPage page, Player player, Map<GUIRegionType, GUIRootRegion> regions) {
        this.page = page;
        this.player = player;
        this.regions = regions;

        this.inventory = page.getWindowProvider().createWindow(player);
    }

    public GUIRegionView getRegionView(GUIRegionType slotType) {
        GUIRootRegion region = regions.get(slotType);
        if (region == null)
            throw new IllegalArgumentException("Attempted to view a non-existing region: '" + slotType.name() + "'");

        return new GUIRegionView(this, region);
    }

    public void drawAll() {
        regions.values().forEach(this::drawRegion);
    }

    public void drawRegion(GUIRegion region) {
        Bukkit.getScheduler().runTask(WandsPlugin.getInstance(), () -> drawRegion(region, true));
    }

    private void drawRegion(GUIRegion region, boolean updateDynamic) {
        GUIRootRegion root = region.getRootRegion();
        if (root == null)
            throw new IllegalArgumentException("Can't draw rootless region!");

        if (root.getRegionType().getInventoryType() == InventoryType.PLAYER) {
            if (this.stateTracker == null)
                stateTracker = new GUIState();

            PlayerInventory inventory = player.getInventory();
            updateAndGetItems(region, updateDynamic, (slot, item) -> {
                ItemStack previous = inventory.getItem(slot);
                inventory.setItem(slot, item);

                stateTracker.trackChange(slot, previous);
            });
        } else {
            updateAndGetItems(region, updateDynamic, inventory::setItem);
        }
    }

    private void updateAndGetItems(GUIRegion region, boolean updateDynamic, BiConsumer<Integer, ItemStack> consumer) {
        region.getItems(consumer);

        if (!updateDynamic)
            return;

        if (dynamicButtons == null)
            dynamicButtons = new IdentityHashMap<>();

        GUIRegionView view = new GUIRegionView(this, region);
        GUIRootRegion root = region.getRootRegion();

        root.getDynamicContents(view, player, (slot, button) -> {
            consumer.accept(slot, button.getItem());
            dynamicButtons.computeIfAbsent(root.getRegionType(), k -> new HashMap<>())
                    .put(slot, button.getAction());
        });
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void restoreState() {
        if (stateTracker == null)
            return;

        List<Item> dropped = stateTracker.restore(player);
        ItemPickupProtection.protect(dropped, player);
    }

    public GUIPage getPage() {
        return page;
    }

}
