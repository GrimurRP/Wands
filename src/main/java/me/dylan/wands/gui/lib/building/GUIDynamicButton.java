package me.dylan.wands.gui.lib.building;

import me.dylan.wands.gui.lib.actions.GUIClickAction;
import org.bukkit.inventory.ItemStack;

public class GUIDynamicButton {
    private final ItemStack item;
    private final GUIClickAction action;

    public GUIDynamicButton(ItemStack item, GUIClickAction action) {
        this.item = item;
        this.action = action;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public GUIClickAction getAction() {
        return action;
    }

}
