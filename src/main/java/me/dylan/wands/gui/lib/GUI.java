package me.dylan.wands.gui.lib;

import me.dylan.wands.gui.lib.views.GUIPageView;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class GUI implements Listener {
    private Plugin plugin;
    private Map<Player, GUISession> sessions;

    private GUIPage rootPage;

    public GUI(Plugin plugin, GUIPage rootPage) {
        this.plugin = plugin;
        this.sessions = new HashMap<>();

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void openFor(Player player) {
        showPage(rootPage, player);
    }

    void showPage(GUIPage page, Player player) {
        GUIPageView view = page.createView(player);

        GUISession session = sessions.computeIfAbsent(player, k -> new GUISession(player));
        GUIPageView previousView = session.getCurrentView();
        if (previousView != null)
            previousView.restoreState();

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
        GUISession session = sessions.get(event.getPlayer());

        GUIPageView view = session.getCurrentView();
        if (view != null)
            view.restoreState();
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

        view.getPage().onClick(bukkitEvent, session);
    }


}
