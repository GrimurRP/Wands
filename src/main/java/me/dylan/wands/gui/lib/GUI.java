package me.dylan.wands.gui.lib;

import me.dylan.wands.gui.lib.views.GUIPageView;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class GUI implements Listener {
    private Plugin plugin;
    private Map<Player, GUISession> sessions;

    private GUIPage rootPage;

    private boolean ignoreCloseEvent = true;

    public GUI(Plugin plugin, GUIPage rootPage) {
        this.plugin = plugin;
        this.sessions = new HashMap<>();
        this.rootPage = rootPage;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public Plugin getOwningPlugin() {
        return plugin;
    }

    public void openFor(Player player) {
        openPage(rootPage, player);
    }

    public void openPage(GUIPage page, Player player) {
        GUIPageView view = page.createView(player);

        GUISession session = sessions.computeIfAbsent(player, k -> new GUISession(player));
        GUIPageView previousView = session.getCurrentView();
        if (previousView != null) {
            ignoreCloseEvent = true; // An inventory page was open: don't unregister everything when the close event fires for that
            previousView.restoreState();
        }

        session.setCurrentPage(view);
        view.drawAll();

        player.openInventory(view.getInventory());
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        GUISession session = sessions.remove(event.getPlayer());
        if (session == null)
            return;

        GUIPageView view = session.getCurrentView();
        if (view != null)
            view.restoreState(); // If player inventory was affected, restore normal player inventory

        session.cleanup(); // Release data stores & destroy session
    }

    @EventHandler
    private void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        GUISession session = sessions.get(player);
        if (session == null)
            return;

        GUIPageView view = session.getCurrentView();
        if (view != null)
            view.restoreState();

        if (ignoreCloseEvent) {
            ignoreCloseEvent = false;
            return;
        }

        sessions.remove(player);
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        GUISession session = sessions.get(player);
        if (session != null) {
            handleClickEvent(session, event);
        }
    }

    private void handleClickEvent(GUISession session, InventoryClickEvent bukkitEvent) {
        GUIPageView view = session.getCurrentView();
        if (view == null)
            return;

        view.getPage().onClick(bukkitEvent, this, session);
    }

    @EventHandler
    private void onItemDrag(InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();

        GUISession session = sessions.get(player);
        if (session != null) {
            handleDragEvent(session, event);
        }
    }

    private void handleDragEvent(GUISession session, InventoryDragEvent event) {
        GUIPageView view = session.getCurrentView();
        if (view == null)
            return;

        view.getPage().onDrag(event, this, session);
    }

}
