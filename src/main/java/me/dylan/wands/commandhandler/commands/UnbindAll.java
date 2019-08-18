package me.dylan.wands.commandhandler.commands;

import me.dylan.wands.Main;
import me.dylan.wands.commandhandler.BaseCommand;
import me.dylan.wands.spell.SpellCompound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class UnbindAll extends BaseCommand {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (isPlayer(sender)) {
            Player player = (Player) sender;
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (isWand(sender, itemStack)) {
                String itemName = itemStack.getItemMeta().getDisplayName();
                SpellCompound compound = new SpellCompound(itemStack);
                if (compound.clear()) {
                    compound.apply(itemStack);
                    sender.sendMessage(Main.PREFIX + "Successfully removed all spells from " + itemName);
                } else {
                    sender.sendMessage(Main.PREFIX + itemName + " §ris already empty!");
                }
            }
        }
        return true;
    }
}
