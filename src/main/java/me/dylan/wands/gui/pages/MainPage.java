package me.dylan.wands.gui.pages;

import me.dylan.wands.WandsPlugin;
import me.dylan.wands.gui.lib.GUI;
import me.dylan.wands.gui.lib.GUIPage;
import me.dylan.wands.gui.lib.actions.GUIClickAction;
import me.dylan.wands.gui.lib.building.GUIBuilder;
import me.dylan.wands.gui.lib.building.GUIChestPageBuilder;
import me.dylan.wands.gui.lib.regions.GUIRootRegion;
import me.dylan.wands.spell.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MainPage {

    public static GUI createGUI() {
        GUIBuilder builder = new GUIBuilder();
        return builder.build(createPage(builder), WandsPlugin.getInstance());
    }

    private static ItemStack createIcon() {
        return ItemBuilder.from(Material.MAP)
                .named(ChatColor.GOLD + "Plugin Info")
                .hideFlags()
                .withLore(ChatColor.GRAY + "Version: " + WandsPlugin.getInstance().getDescription().getVersion())
                .build();
    }

    private static GUIPage createPage(GUIBuilder builder) {
        GUIChestPageBuilder pageBuilder = builder.newChestPage("Main menu", 3);
        GUIRootRegion root = pageBuilder.getRootRegion()
            .setProtected(true)
            .setItem(ItemBuilder.from(Material.LIGHT_GRAY_STAINED_GLASS_PANE).named(" ").build());

        root.getSlot(1, 1)
                .setItem(createIcon());

        GUIPage configPage = ConfigPage.createPage(builder);
        GUIPage creatorPage = WandCreatorPage.createPage(builder);
        GUIPage wandListPage = WandListPage.createPage(builder);

        // Config
        root.getSlot(3, 1)
                .setItem(ConfigPage.createIcon())
                .setClickAction(GUIClickAction.openPage(configPage));

        // Wand creator page
        root.getSlot(5, 1)
                .setItem(WandCreatorPage.createIcon())
                .setClickAction(GUIClickAction.openPage(creatorPage));

        // Wand list page
        root.getSlot(7, 1)
                .setItem(WandListPage.createIcon())
                .setClickAction(GUIClickAction.openPage(wandListPage));

        return pageBuilder.build();
    }



}
