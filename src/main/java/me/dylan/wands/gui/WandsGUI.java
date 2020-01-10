package me.dylan.wands.gui;

import me.dylan.wands.WandsPlugin;
import me.dylan.wands.gui.lib.GUI;
import me.dylan.wands.gui.lib.building.GUIBuilder;
import me.dylan.wands.gui.lib.building.GUIChestPageBuilder;
import me.dylan.wands.gui.lib.regions.GUIRegion;
import me.dylan.wands.gui.lib.regions.GUIRootRegion;
import me.dylan.wands.gui.lib.regions.dynamic.GUIContentProvider;
import me.dylan.wands.gui.lib.views.GUIRegionView;
import me.dylan.wands.spell.SpellCompound;
import me.dylan.wands.spell.SpellType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class WandsGUI {

    private static final GUI ROOT_PAGE = createWandsGUI();

    public static void showTo(Player player) {
        ROOT_PAGE.openFor(player);
    }

    /*
    /wands
        info version
        config
            enable magic
            require permission
            global cooldown
            list spells
                spell info
                specific cooldown
        get wand
            list wands
            edit order
            add/remove wand
        player.holdsWand() ? update wand : create wand
            change order spells
            remove spell
            add spell
            rename
     */
    private static GUI createWandsGUI() {
        GUIBuilder builder = new GUIBuilder();

        GUIChestPageBuilder rootBuilder = builder.newChestPage("Wands");
        GUIRootRegion rootRegion = rootBuilder.getRootRegion();

        rootRegion.addRegion(0, 0, 9, 3) // 9x3 area
                .setProtected(true)
                .setItem(new ItemStack(Material.LIME_STAINED_GLASS))
                .setAction((view, item, player) -> {
                    player.sendMessage("Can't click here!");
                    return true;
                });

        GUIRegion wandRegion = rootRegion.addDynamicRegion(2, 0, 7, 3)
                .setProtected(true)
                .setItem(null)
                .setContentProvider(GUIContentProvider.of(WandsGUI::listSpells, (region, item, player) -> {
                    player.sendMessage("Clicked " + item.getItemMeta().getDisplayName());
                    return true;
                }));

        rootRegion.getSlot(0, 1)
                .setProtected(false)
                .setItem(null)
                .setObserver((view, from, to) -> {
                    view.drawRegion(wandRegion);
                    return true;
                });

        return builder.build(rootBuilder, JavaPlugin.getPlugin(WandsPlugin.class));
    }

    private static List<ItemStack> listSpells(GUIRegionView view, Player p) {
        ItemStack wand = view.viewSlot(0, 1).getItem();
        if (wand == null)
            return Collections.emptyList();

        return SpellCompound.getCompound(wand).stream()
                .map(WandsGUI::spellTypeToItemStack)
                .collect(Collectors.toList());
    }

    private static ItemStack spellTypeToItemStack(SpellType type) {
        return null;
    }

}
