package me.dylan.wands.commandhandler;

import me.dylan.wands.WandsPlugin;
import me.dylan.wands.spell.SpellType;
import me.dylan.wands.spell.accessories.ItemTag;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class CommandUtils {

    private CommandUtils() {
    }

    public static boolean isPlayerOrNotify(CommandSender sender) {
        if (sender instanceof Player) {
            return true;
        } else {
            sender.sendMessage(WandsPlugin.PREFIX + "§cOnly players can perform this command!");
            return false;
        }
    }

    public static boolean isWandOrNotify(CommandSender sender, ItemStack itemStack) {
        if (ItemTag.IS_WAND.isTagged(itemStack)) {
            return true;
        } else {
            sender.sendMessage(WandsPlugin.PREFIX + "Held item is not a wand!");
            return false;
        }
    }

    public static boolean checkPermOrNotify(@NotNull CommandSender commandSender, String permission) {
        if (commandSender.hasPermission(permission)) {
            return true;
        } else {
            commandSender.sendMessage("§cYou require §r" + permission);
            return false;
        }
    }

    @Contract("_, null, _ -> false")
    public static boolean isSpellOrNotify(@NotNull CommandSender commandSender, @Nullable SpellType spellType, String argument) {
        if (spellType == null) {
            commandSender.sendMessage(WandsPlugin.PREFIX + "§7§l" + argument + " §ris not a spell!");
            return false;
        }
        return true;
    }

    public static boolean isInRangeOrNotify(@NotNull CommandSender commandSender, int in) {
        if (in >= 0 && in <= 99) {
            return true;
        } else {
            commandSender.sendMessage(in + " must be a number between " + 0 + " and " + 99);
            return false;
        }
    }

    public static List<String> validCompletions(@NotNull String toComplete, String... values) {
        String toCompleteLowered = toComplete.toLowerCase();
        return Arrays.stream(values)
                .map(String::toLowerCase)
                .filter(s -> s.contains(toCompleteLowered))
                .collect(Collectors.toList());
    }
}
