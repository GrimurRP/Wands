package me.dylan.wands.gui.lib.regions.dynamic;

import me.dylan.wands.gui.lib.actions.GUIClickAction;
import me.dylan.wands.gui.lib.building.GUIDynamicButton;
import me.dylan.wands.gui.lib.views.GUIRegionView;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public interface GUIContentProvider {
    List<GUIDynamicButton> getContents(GUIRegionView view, Player player);

    static GUIContentProvider of(Collection<ItemStack> items) {
        return of(items, null);
    }

    static GUIContentProvider of(Collection<ItemStack> items, GUIClickAction action) {
        List<GUIDynamicButton> list = items.stream()
                .map(item -> new GUIDynamicButton(item, action))
                .collect(Collectors.toList());
        return (view, player) -> list;
    }

    static GUIContentProvider of(Supplier<Collection<ItemStack>> supplier) {
        return of(supplier, null);
    }

    static GUIContentProvider of(Supplier<Collection<ItemStack>> supplier, GUIClickAction action) {
        return (view, player) -> supplier.get().stream()
                .map(item -> new GUIDynamicButton(item, action))
                .collect(Collectors.toList());
    }

    static GUIContentProvider of(BiFunction<GUIRegionView, Player, Collection<ItemStack>> fn) {
        return of(fn, null);
    }

    static GUIContentProvider of(BiFunction<GUIRegionView, Player, Collection<ItemStack>> fn, GUIClickAction action) {
        return (view, player) -> fn.apply(view, player).stream()
                .map(item -> new GUIDynamicButton(item, action))
                .collect(Collectors.toList());
    }
}
