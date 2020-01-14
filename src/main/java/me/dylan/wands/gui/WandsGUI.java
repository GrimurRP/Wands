package me.dylan.wands.gui;

import me.dylan.wands.WandsPlugin;
import me.dylan.wands.gui.lib.GUI;
import me.dylan.wands.gui.lib.GUIPage;
import me.dylan.wands.gui.lib.building.GUIBuilder;
import me.dylan.wands.gui.lib.building.GUIChestPageBuilder;
import me.dylan.wands.gui.lib.regions.GUIRegion;
import me.dylan.wands.gui.lib.regions.GUIRootRegion;
import me.dylan.wands.gui.lib.regions.dynamic.GUIContentProvider;
import me.dylan.wands.gui.lib.views.GUIRegionView;
import me.dylan.wands.gui.pages.MainPage;
import me.dylan.wands.spell.ItemBuilder;
import me.dylan.wands.spell.SpellCompound;
import me.dylan.wands.spell.SpellType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
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

public class WandsGUI {

    private static final GUI ROOT_PAGE = MainPage.createGUI();

    public static void showTo(Player player) {
        ROOT_PAGE.openFor(player);
    }

    private static GUI createWandsGUI() {
        GUIBuilder builder = new GUIBuilder();

        GUIChestPageBuilder spellInspectionBuilder = builder.newChestPage("Wands", 3);
        GUIRootRegion spellInspectionRoot = spellInspectionBuilder.getRootRegion();

        spellInspectionRoot.addRegion(0, 0, 9, 3) // 9x3 area
                .setProtected(true)
                .setItem(ItemBuilder.from(Material.WHITE_STAINED_GLASS_PANE).named(ChatColor.WHITE + "Place a wand in the middle!").build())
                .setAction((gui, view, item, player) -> {
                    player.sendMessage("Can't click here!");
                    return true;
                });

        GUIRegion wandRegion = spellInspectionRoot.addDynamicRegion(3, 0, 6, 3)
                .setProtected(true)
                .setItem(ItemBuilder.from(Material.LIGHT_GRAY_STAINED_GLASS_PANE).named("").build())
                .setContentProvider(GUIContentProvider.of(WandsGUI::listSpells))
                .setAction((gui, region, item, player) -> {
                    if (item != null)
                        player.sendMessage("Clicked " + item.getItemMeta().getDisplayName());
                    return true;
                });

        spellInspectionRoot.getSlot(1, 1)
                .setProtected(false)
                .setItem(null)
                .setClickAction(null)
                .setObserver((view, from, to) -> {
                    view.drawRegion(wandRegion);
                    return false;
                });

        GUIPage spellInspectionPage = spellInspectionBuilder.build();



        GUIChestPageBuilder rootBuilder = builder.newChestPage("Routing page", 3); // 3 rows

        GUIRootRegion rootRegion = rootBuilder.getRootRegion();
        rootRegion.setProtected(true);

        rootRegion.getSlot(1, 1)
                .setItem(new ItemStack(Material.PAPER))
                .setClickAction((gui, view, item, player) -> {
                    gui.openPage(spellInspectionPage, player);
                    return true;
                });

        return builder.build(rootBuilder.build(), JavaPlugin.getPlugin(WandsPlugin.class));
    }

    private static List<ItemStack> listSpells(GUIRegionView view, Player p) {
        view = view.getRootView();
        ItemStack wand = view.getPage().getItem(10);
        if (wand == null) {
            return Collections.emptyList();
        }

        List<ItemStack> spells = SpellCompound.getCompound(wand).stream()
                .map(WandsGUI::spellTypeToItemStack)
                .collect(Collectors.toList());

        return spells;
    }

    private static ItemStack spellTypeToItemStack(SpellType type) {
        List<String> loreLines = Arrays.asList(type.behavior.toString().split("\n"));

        ItemStack item = new ItemStack(type.material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + type.getDisplayName());
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.setLore(loreLines);
        item.setItemMeta(meta);
        return item;
    }

}
