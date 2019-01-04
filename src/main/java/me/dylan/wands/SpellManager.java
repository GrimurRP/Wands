package me.dylan.wands;

import net.minecraft.server.v1_13_R2.ChatComponentText;
import net.minecraft.server.v1_13_R2.ChatMessageType;
import net.minecraft.server.v1_13_R2.PacketPlayOutChat;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public final class SpellManager implements Listener {

    @EventHandler
    public void playerInteractEvent(PlayerInteractEvent event) {
        if (!Wands.ENABLED) return;
        Player player = event.getPlayer();
        ItemStack handItem = player.getInventory().getItemInMainHand();
        if (handItem != null) {
            WandItem tool = new WandItem(handItem);
            if (tool.isMarkedAsWand()) {
                event.setCancelled(true);
                Action a = event.getAction();
                if (event.getHand().equals(EquipmentSlot.HAND)) {
                    if (a == Action.LEFT_CLICK_AIR || a == Action.LEFT_CLICK_BLOCK) {
                        onCast(player);
                    } else if (a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK) {
                        onSelect(player);
                    }
                }
            }
        }
    }

    private void onSelect(Player player) {
        ItemStack hand = player.getInventory().getItemInMainHand();

        WandItem wandItem = new WandItem(hand);
        int index = wandItem.getSpellIndex();
        int maxValue = wandItem.getSpellSize();

        if (maxValue == 0) return;

        if (!player.isSneaking()) {
            index = index < maxValue ? index + 1 : 1;
        } else {
            index = index > 1 ? index - 1 : maxValue;
        }
        wandItem.setSpellIndex(index);

        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 0.5F, 0.5F);
        sendActionBar((CraftPlayer) player,
                ChatColor.translateAlternateColorCodes('&',
                        "&6Current spell: &7&l" + wandItem.getSelectedSpell().getName()));
    }

    private void onCast(Player player) {
        WandItem wandItem = new WandItem(player.getInventory().getItemInMainHand());
        wandItem.getSelectedSpell().cast(player);
    }

    private void sendActionBar(CraftPlayer player, String message) {
        if (player.getHandle().playerConnection != null && message != null && !message.isEmpty()) {
            player.getHandle().playerConnection.sendPacket(new PacketPlayOutChat(new ChatComponentText(message), ChatMessageType.GAME_INFO));
        }
    }
}

