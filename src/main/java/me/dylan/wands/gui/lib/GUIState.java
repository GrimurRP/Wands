package me.dylan.wands.gui.lib;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GUIState {
    private Map<Integer, ItemStack> originalState; // Lazily initialized

    public void trackChange(int slot, ItemStack newItem) {
        originalState.put(slot, newItem);
    }

    public List<Item> restore(Player player) {
        if (originalState == null)
            return Collections.emptyList();

        PlayerInventory inventory = player.getInventory();

        List<ItemStack> extra = new ArrayList<>();
        originalState.forEach((slot, item) -> {
            ItemStack original = inventory.getItem(slot);
            if (original != null) {
                extra.add(item);
                return;
            }
            inventory.setItem(slot, item);
        });

        Map<Integer, ItemStack> leftover = inventory.addItem(extra.toArray(new ItemStack[0]));
        List<Item> entities = leftover.values().stream()
                .map(item -> player.getWorld().dropItemNaturally(player.getLocation(), item))
                .collect(Collectors.toList());

        originalState.clear();
        return entities;
    }
}
