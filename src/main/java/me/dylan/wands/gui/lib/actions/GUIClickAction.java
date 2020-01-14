package me.dylan.wands.gui.lib.actions;

import me.dylan.wands.gui.lib.GUI;
import me.dylan.wands.gui.lib.GUIPage;
import me.dylan.wands.gui.lib.views.GUIRegionView;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface GUIClickAction {
    boolean onClick(GUI gui, GUIRegionView regionView, ItemStack clickedItem, Player player);

    static GUIClickAction openPage(GUIPage page) {
        return (gui, region, item, player) -> {
            gui.openPage(page, player);
            return false;
        };
    }

}
