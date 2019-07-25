package me.dylan.wands.pluginmeta;

import me.dylan.wands.Main;
import me.dylan.wands.customitems.AssassinDagger;
import me.dylan.wands.customitems.CursedBow;
import me.dylan.wands.spell.BrowseParticle;
import me.dylan.wands.spell.SpellManagementUtil;
import me.dylan.wands.spell.SpellType;
import me.dylan.wands.util.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

@SuppressWarnings("unused")
public enum ObtainableItem {
    ASSASSINS_DAGGER(ItemUtil.getItemStack(() -> {
        ItemStack itemStack = new ItemStack(Material.MUSIC_DISC_MALL);
        SpellManagementUtil.setAsUnregisterable(itemStack);
        ItemUtil.setName(itemStack, "&dAssassin's &8Dagger");
        ItemUtil.setPersistentData(itemStack, AssassinDagger.ID_TAG, PersistentDataType.BYTE, (byte) 1);
        return itemStack;
    })),

    CURSED_BOW(ItemUtil.getItemStack(() -> {
        ItemStack itemStack = new ItemStack(Material.BOW);
        SpellManagementUtil.setAsUnregisterable(itemStack);
        ItemUtil.setName(itemStack, "&cCursed Bow");
        ItemUtil.setItemMeta(itemStack, meta -> {
            meta.setUnbreakable(true);
            meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
            meta.addItemFlags(ItemFlag.values());
        });
        ItemUtil.setPersistentData(itemStack, CursedBow.ID_TAG, PersistentDataType.BYTE, (byte) 1);
        return itemStack;
    })),

    MAGIC_WAND(ItemUtil.getItemStack(() -> {
        ItemStack itemStack = new ItemStack(Material.STICK);
        ItemUtil.setName(itemStack, "&7Magic Wand");
        SpellManagementUtil.setAsWand(itemStack);
        SpellManagementUtil.setSpells(itemStack,
                SpellType.COMET,
                SpellType.SPARK,
                SpellType.CONFUSE,
                SpellType.LAUNCH,
                SpellType.POISON_WAVE
        );
        SpellManagementUtil.setSpellBrowseParticles(itemStack, BrowseParticle.DEFAULT);
        return itemStack;
    })),

    BLOOD_WAND(ItemUtil.getItemStack(() -> {
        ItemStack itemStack = new ItemStack(Material.NETHER_WART);
        ItemUtil.setName(itemStack, "&cBlood Magic");
        SpellManagementUtil.setAsWand(itemStack);
        SpellManagementUtil.setSpells(itemStack,
                SpellType.BLOOD_WAVE,
                SpellType.BLOOD_SPARK,
                SpellType.BLOOD_EXPLODE,
                SpellType.BLOOD_STUN,
                SpellType.BLOOD_BLOCK
        );
        SpellManagementUtil.setSpellBrowseParticles(itemStack, BrowseParticle.PARTICLE_BLOOD);
        return itemStack;
    })),

    ICE_WAND(ItemUtil.getItemStack(() -> {
        ItemStack itemStack = new ItemStack(Material.BLAZE_ROD);
        ItemUtil.setName(itemStack, "&rIce Wand");
        SpellManagementUtil.setAsWand(itemStack);
        SpellManagementUtil.setSpells(itemStack,
                SpellType.THUNDER_ARROW,
                SpellType.THUNDER_STRIKE,
                SpellType.THUNDER_STORM,
                SpellType.THUNDER_RAGE,
                SpellType.ICE_FREEZE,
                SpellType.ICE_AURA
        );
        SpellManagementUtil.setSpellBrowseParticles(itemStack, BrowseParticle.PARTICLE_ICE);
        return itemStack;
    })),

    DARK_WAND(ItemUtil.getItemStack(() -> {
        ItemStack itemStack = new ItemStack(Material.STICK);
        ItemUtil.setName(itemStack, "&8&lDark Wand");
        SpellManagementUtil.setAsWand(itemStack);
        SpellManagementUtil.setSpells(itemStack,
                SpellType.DARK_PULSE,
                SpellType.DARK_BLOCK,
                SpellType.DARK_CIRCLE,
                SpellType.DARK_PUSH,
                SpellType.DARK_AURA,
                SpellType.DARK_SPARK
        );
        SpellManagementUtil.setSpellBrowseParticles(itemStack, BrowseParticle.PARTICLE_DARK);
        return itemStack;
    })),
    CELCRIUS_WAND(ItemUtil.getItemStack(() -> {
        ItemStack itemStack = new ItemStack(Material.BLAZE_POWDER);
        ItemUtil.setName(itemStack, "&6Celcrius Wand");
        SpellManagementUtil.setAsWand(itemStack);
        SpellManagementUtil.setSpells(itemStack,
                SpellType.FIRE_COMET,
                SpellType.FIRE_TWISTER,
                SpellType.FLAME_WAVE,
                SpellType.FLAME_SHOCK_WAVE,
                SpellType.FLAME_THROWER,
                SpellType.FIRE_SPARK
        );
        SpellManagementUtil.setSpellBrowseParticles(itemStack, BrowseParticle.PARTICLE_FIRE);
        return itemStack;
    })),
    CORRUPTED_WAND(ItemUtil.getItemStack(() -> {
        ItemStack itemStack = new ItemStack(Material.STICK);
        ItemUtil.setName(itemStack, "&cCorrupted Wand");
        SpellManagementUtil.setAsWand(itemStack);
        SpellManagementUtil.setSpells(itemStack,
                SpellType.CORRUPTED_RAIN,
                SpellType.CORRUPTED_WAVE,
                SpellType.CORRUPTED_SHOCK_WAVE,
                SpellType.CORRUPTED_WOLFS,
                SpellType.CORRUPTED_LAUNCH,
                SpellType.CORRUPTED_SPARK,
                SpellType.POISON_WAVE
        );
        SpellManagementUtil.setSpellBrowseParticles(itemStack, BrowseParticle.PARTICLE_CORRUPTED);
        return itemStack;
    })),
    JETP_WAND(ItemUtil.getItemStack(() -> {
        ItemStack itemStack = new ItemStack(Material.POTATO);
        ItemUtil.setName(itemStack, "&4&lE&c&lp&6&li&e&lc&2&l &a&lJ&b&le&3&lt&1&lp&9&l &d&lW&4&la&c&ln&6&ld");
        ItemUtil.setLore(itemStack, "A wand containing spells", "which are created by ", "jetp250");
        SpellManagementUtil.setAsWand(itemStack);
        SpellManagementUtil.setSpells(itemStack, SpellType.JETP);
        return itemStack;
    }));

    private static final String[] names = Arrays.stream(values()).map(Enum::toString).toArray(String[]::new);

    private final ItemStack itemStack;

    ObtainableItem(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public static String[] getNames() {
        return names;
    }

    public static void openInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, InventoryType.CHEST, Main.PREFIX);
        for (ObtainableItem obtainableItem : ObtainableItem.values()) {
            inventory.addItem(obtainableItem.itemStack);
        }
        player.openInventory(inventory);
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}