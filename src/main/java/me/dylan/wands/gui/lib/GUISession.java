package me.dylan.wands.gui.lib;

import me.dylan.wands.gui.lib.data.GUIDataStore;
import me.dylan.wands.gui.lib.views.GUIPageView;
import org.bukkit.entity.Player;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class GUISession {
    private static final class PageAndData {
        private final GUIPageView view;
        private final GUIDataStore data;

        public PageAndData(GUIPageView view) {
            this.view = view;
            this.data = new GUIDataStore();
        }
    }

    private static final Map<Player, GUISession> SESSIONS = new HashMap<>();

    private final Queue<PageAndData> history;
    private PageAndData currentPage;

    private final GUIDataStore persistentData;

    private final Player player;

    public GUISession(Player player) {
        this.history = new ArrayDeque<>();
        this.persistentData = new GUIDataStore();

        this.player = player;

        SESSIONS.put(player, this);
    }

    public GUIPageView getCurrentView() {
        return this.currentPage.view;
    }

    public GUIDataStore getLocalData() {
        return (currentPage == null) ? null : currentPage.data;
    }

    public GUIDataStore getPersistentData() {
        return persistentData;
    }

    public GUIPageView getPreviousView() {
        PageAndData item = history.peek();
        return (item == null) ? null : item.view;
    }

    public void setCurrentPage(GUIPageView page) {
        PageAndData last = history.peek();
        if (last != null && last.view == page) {
            currentPage = history.remove();
        } else {
            history.add(currentPage);
            currentPage = new PageAndData(page);
        }
    }

    void cleanup() {
        SESSIONS.remove(player);
    }

    public static GUISession getSession(Player player) {
        return SESSIONS.get(player);
    }

}
