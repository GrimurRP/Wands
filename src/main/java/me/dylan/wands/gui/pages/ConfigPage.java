package me.dylan.wands.gui.pages;

import me.dylan.wands.WandsPlugin;
import me.dylan.wands.config.ConfigHandler;
import me.dylan.wands.gui.lib.GUIPage;
import me.dylan.wands.gui.lib.building.GUIBuilder;
import me.dylan.wands.gui.lib.building.GUIChestPageBuilder;
import me.dylan.wands.gui.lib.regions.GUIRootRegion;
import me.dylan.wands.spell.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ConfigPage {

    public static ItemStack createIcon() {
        return ItemBuilder.from(Material.WRITABLE_BOOK)
                .named(ChatColor.GOLD + "Plugin Configuration")
                .withLore(ChatColor.GRAY + "Configure the plugin")
                .hideFlags()
                .build();
    }

    public static GUIPage createPage(GUIBuilder guiBuilder) {
        GUIChestPageBuilder pageBuilder = guiBuilder.newChestPage("Wand configuration", 3);
        GUIRootRegion rootRegion = pageBuilder.getRootRegion();

        ConfigHandler config = WandsPlugin.getInstance().getConfigHandler();

        //createBoolean(0, 0, config.doesCastingRequirePermission(), rootRegion, config::requirePermissionForCasting);

        return pageBuilder.build();
    }

}
