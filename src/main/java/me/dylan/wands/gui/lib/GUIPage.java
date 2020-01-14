package me.dylan.wands.gui.lib;

import me.dylan.wands.WandsPlugin;
import me.dylan.wands.gui.lib.observers.GUIRegionObserver;
import me.dylan.wands.gui.lib.regions.GUIRegion;
import me.dylan.wands.gui.lib.regions.GUIRegionType;
import me.dylan.wands.gui.lib.regions.GUIRootRegion;
import me.dylan.wands.gui.lib.views.GUIPageView;
import me.dylan.wands.gui.lib.views.GUIRegionView;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public final class GUIPage {
    private final Map<GUIRegionType, GUIRootRegion> regions;
    private final GUIWindowProvider windowProvider;

    public GUIPage(Map<GUIRegionType, GUIRootRegion> regions, GUIWindowProvider windowProvider) {
        if (regions != null)
            this.regions = new IdentityHashMap<>(regions);
        else
            this.regions = Collections.emptyMap();

        this.windowProvider = windowProvider;
    }

    public GUIWindowProvider getWindowProvider() {
        return windowProvider;
    }

    public GUIPageView createView(Player player) {
        return new GUIPageView(this, player, regions);
    }

    void onClick(InventoryClickEvent event, GUI gui, GUISession session) {



        handleClickEvent(event, session, gui);
    }

    private void handleClickEvent(InventoryClickEvent event, GUISession session, GUI gui) {
        boolean cancelled = event.isCancelled();

        int slot = event.getSlot();
        GUIRootRegion root = getClickedRootRegion(event.getClickedInventory(), event.getView(), event.getSlot());;
        Bukkit.broadcastMessage("Action: " + event.getAction().name());
        if (root == null && event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            Bukkit.broadcastMessage("Here. Original slot: " + slot);

            Inventory other = event.getClickedInventory() == event.getView().getBottomInventory()
                    ? event.getView().getTopInventory() : event.getView().getBottomInventory();

            Bukkit.broadcastMessage("Clicked on " + (other == event.getView().getTopInventory() ? "bottom inventory" : "top inventory"));

            slot = findFirstFreeSlot(root, other);
            if (slot == -1) {
                Bukkit.broadcastMessage("No slot found");
                event.setCancelled(true);
                return;
            }
            Bukkit.broadcastMessage("Found slot: " + slot);

            root = getClickedRootRegion(other, event.getView(), slot);
        }

        if (root == null) {
            Bukkit.broadcastMessage("Root was null");
            return;
        }

        Bukkit.broadcastMessage("Root was not null");

        GUIRegion clicked = root.getRegionAtSlot(slot);
        if (clicked == null) {
            Bukkit.broadcastMessage("Clicked region was null");
            return;
        }

        cancelled |= clicked.isProtected();

        if (clicked.getAction() != null) {
            GUIRegionView view = session.getCurrentView().getRegionView(root.getRegionType());
            cancelled |= clicked.getAction().onClick(gui, view, event.getCurrentItem(), (Player) event.getWhoClicked());
        }

        ItemStack from = event.getCurrentItem();
        ItemStack to = event.getCursor();

        GUIPageView pageView = session.getCurrentView();
        cancelled |= root.notifyObservers(slot, from, to, new GUIRegionView(pageView, clicked));

        event.setCancelled(cancelled);
    }

    private int findFirstFreeSlot(GUIRegion region, Inventory target) {
        int width = region.getWidth();
        int height = region.getHeight();

        int index = 0;
        for (int j = 0; j < height; ++j) {
            for (int i = 0; i < width; ++i) {
                GUIRegion subregion = region.getRegionAt(i, j);
                if (!subregion.isProtected() && target.getItem(index) == null) {
                    return index;
                }
                index++;
            }
        }
        return -1;
    }

    void onDrag(InventoryDragEvent event, GUI gui, GUISession session) {
        InventoryView inventoryView = event.getView();

        Map<Integer, ItemStack> illegal = new HashMap<>();
        ItemStack cursor = event.getOldCursor();
        int amount = 0;

        Iterator<Integer> iterator = event.getRawSlots().iterator();
        while (iterator.hasNext()) {
            int rawSlot = iterator.next();
            Inventory inventory = inventoryView.getInventory(rawSlot);
            int slot = inventoryView.convertSlot(rawSlot);

            GUIRootRegion rootRegion = getClickedRootRegion(inventory, inventoryView, slot);
            if (rootRegion == null)
                continue;

            GUIRegion clickedRegion = rootRegion.getRegionAtSlot(slot);

            if (clickedRegion.isProtected()) {
                illegal.put(rawSlot, inventory.getItem(slot));
                amount += event.getNewItems().get(rawSlot).getAmount();
            } else  {
                boolean cancel = rootRegion.notifyObservers(slot, inventory.getItem(slot), event.getNewItems().get(rawSlot), new GUIRegionView(session.getCurrentView(), clickedRegion));
                if (cancel) {
                    illegal.put(rawSlot, inventory.getItem(slot));
                    amount += event.getNewItems().get(rawSlot).getAmount();
                }
            }
        }

        if (!illegal.isEmpty()) {
            int finalAmount = amount;

            Bukkit.getScheduler().runTask(gui.getOwningPlugin(), () -> {
                illegal.forEach((rawSlot, item) -> {
                   Inventory inventory = inventoryView.getInventory(rawSlot);
                   int slot = inventoryView.convertSlot(rawSlot);
                   inventory.setItem(slot, item);
                });
                cursor.setAmount(finalAmount + event.getWhoClicked().getItemOnCursor().getAmount());
                event.getWhoClicked().setItemOnCursor(cursor);
            });
        }

    }


    private GUIRootRegion getClickedRootRegion(Inventory clicked, InventoryView view, int slot) {
        if (clicked == view.getBottomInventory()) { // if player inventory
            if (GUIRegionType.PLAYER_INVENTORY.containsSlot(slot)) {
                return regions.get(GUIRegionType.PLAYER_INVENTORY);
            }
            return regions.get(GUIRegionType.PLAYER_HOTBAR);
        }

        return getClickedRootRegion(slot);
    }

    private GUIRootRegion getClickedRootRegion(int slot) {
        for (GUIRootRegion region : regions.values()) {
            if (region.containsSlot(slot))
                return region;
        }
        return null;
    }

}
