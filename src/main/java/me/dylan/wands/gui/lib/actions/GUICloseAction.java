package me.dylan.wands.gui.lib.actions;

import me.dylan.wands.gui.lib.GUISession;
import me.dylan.wands.gui.lib.views.GUIPageView;
import org.bukkit.entity.Player;

public interface GUICloseAction {
    boolean onClose(GUISession session, GUIPageView page, Player player);
}
