package me.dylan.wands.gui.lib;

import me.dylan.wands.gui.lib.observers.GUIObserverMap;
import me.dylan.wands.gui.lib.regions.GUIRegion;
import me.dylan.wands.gui.lib.regions.GUIRegionType;
import me.dylan.wands.gui.lib.regions.GUIRootRegion;
import me.dylan.wands.gui.lib.views.GUIPageView;
import me.dylan.wands.gui.lib.views.GUIRegionView;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.IdentityHashMap;
import java.util.Map;

public final class GUIPage {
    private final Map<GUIRegionType, GUIRootRegion> regions;
    private final GUIObserverMap observerMap;
    private final GUI gui;

    GUIPage(GUI gui, GUIObserverMap observerMap, Map<GUIRegionType, GUIRootRegion> regions) {
        this.gui = gui;
        this.observerMap = observerMap;
        this.regions = new IdentityHashMap<>(regions);
    }

    public void openFor(Player player) {
        gui.showPage(this, player);
    }

    public GUIPageView createView(Player player) {
        return new GUIPageView(this, player, regions);
    }

    void onClick(InventoryClickEvent event, GUISession session) {
        GUIRootRegion root = getClickedRootRegion(event);
        if (root == null)
            return;

        GUIRegion clicked = root.getRegionAtSlot(event.getSlot());
        if (clicked == null)
            return;

        handleClickEvent(event, root, clicked, session);
    }

    private void handleClickEvent(InventoryClickEvent event, GUIRootRegion root, GUIRegion clicked, GUISession session) {
        boolean cancelled = event.isCancelled() || clicked.isProtected();

        if (clicked.getAction() != null) {
            GUIRegionView view = session.getCurrentView().getRegionView(root.getRegionType());
            cancelled |= clicked.getAction().onClick(view, event.getCurrentItem(), (Player) event.getWhoClicked());
        }

        ItemStack from = event.getCurrentItem();
        ItemStack to = event.getCursor();

        GUIPageView pageView = session.getCurrentView();
        cancelled |= root.notifyObservers(event.getSlot(), from, to, new GUIRegionView(pageView, clicked));

        event.setCancelled(cancelled);
    }

    private GUIRootRegion getClickedRootRegion(InventoryClickEvent event) {
        if (event.getClickedInventory() == event.getView().getBottomInventory()) { // if player inventory
            if (event.getSlot() > 9)
                return regions.get(GUIRegionType.PLAYER_INVENTORY);
            return regions.get(GUIRegionType.PLAYER_HOTBAR);
        }

        for (GUIRootRegion region : regions.values()) {
            if (region.getRegionType().containsSlot(event.getSlot()))
                return region;
        }
        return null;
    }

}
