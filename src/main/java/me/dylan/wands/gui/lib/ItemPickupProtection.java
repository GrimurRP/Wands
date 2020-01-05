package me.dylan.wands.gui.lib;

import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class ItemPickupProtection implements Listener {
    private static final long PROTECTION_TIME = 2000; // in milliseconds

    private static ItemPickupProtection instance;

    public static void enable(Plugin plugin) {
        if (instance != null)
            return;

        instance = new ItemPickupProtection(plugin);
    }

    public static void protect(List<Item> items, Player owner) {
        if (instance == null)
            return;

        instance.addItems(items, owner);
    }

    private Map<Player, Map<Item, Long>> protections;
    private boolean taskRunning;

    private final Plugin plugin;

    public ItemPickupProtection(Plugin plugin) {
        this.plugin = plugin;
        this.protections = new HashMap<>();

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    private void addItems(List<Item> items, Player owner) {
        Map<Item, Long> expires = protections.computeIfAbsent(owner, k -> new IdentityHashMap<>());
        long expirationDate = System.currentTimeMillis() + PROTECTION_TIME;

        items.forEach(item -> expires.put(item, expirationDate));

        if (!taskRunning) {
            taskRunning = true;
            Bukkit.getScheduler().runTaskTimer(plugin, this::tick, 0L, 200L);
        }
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        protections.remove(event.getPlayer());
    }

    @EventHandler
    private void onItemPickup(EntityPickupItemEvent event) {
        LivingEntity entity = event.getEntity();
        if (!(entity instanceof Player))
            return;

        Player player = (Player) entity;
        Map<Item, Long> itemMap = protections.get(player);
        if (itemMap == null)
            return;

        Long expires = itemMap.get(event.getItem());
        if (expires == null)
            return;

        if (System.currentTimeMillis() > expires) {
            itemMap.remove(event.getItem());
            return;
        }

        event.setCancelled(true);
    }

    private void tick(BukkitTask task) {
        long time = System.currentTimeMillis();

        Iterator<Map.Entry<Player, Map<Item, Long>>> iterator = protections.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry<Player, Map<Item, Long>> entry = iterator.next();

            Iterator<Map.Entry<Item, Long>> itemIterator = entry.getValue().entrySet().iterator();
            while(iterator.hasNext()) {
                Map.Entry<Item, Long> itemLongEntry = itemIterator.next();
                if (itemLongEntry.getValue() < time || !itemLongEntry.getKey().isValid()) {
                    iterator.remove();
                }
            }

            if (entry.getValue().isEmpty()) {
                iterator.remove();
            }
        };

        if (protections.isEmpty()) {
            task.cancel();
            taskRunning = false;
        }
    }

}
