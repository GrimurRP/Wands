package me.dylan.wands.gui.lib.views;

import me.dylan.wands.gui.lib.GUISlot;
import org.bukkit.inventory.ItemStack;

public class GUISlotView {

    private final GUISlot slot;

    public GUISlotView(GUISlot slot) {
        this.slot = slot;
    }

    public ItemStack getItem() {
        return slot.getItem();
    }

    public boolean isProtected() {
        return slot.isProtected();
    }

}
