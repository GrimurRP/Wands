package me.dylan.wands.gui.pages;

import me.dylan.wands.gui.lib.GUIPage;
import me.dylan.wands.gui.lib.building.GUIBuilder;
import me.dylan.wands.gui.lib.building.GUIChestPageBuilder;
import me.dylan.wands.spell.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class WandListPage {

    public static ItemStack createIcon() {
        return ItemBuilder.from(Material.BOOK)
                .named(ChatColor.GOLD + "View Wands")
                .withLore(ChatColor.GRAY + "See and get the existing wands")
                .hideFlags()
                .build();
    }

    public static GUIPage createPage(GUIBuilder builder) {
        GUIChestPageBuilder pageBuilder = builder.newChestPage("Get Wand", 3);

        return pageBuilder.build();
    }

}
