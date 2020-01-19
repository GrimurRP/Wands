package me.dylan.wands.commandhandler.commands;

import me.dylan.wands.WandsPlugin;
import me.dylan.wands.commandhandler.CommandUtils;
import me.dylan.wands.config.ConfigHandler;
import me.dylan.wands.spell.SpellType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class TweakCooldown implements CommandExecutor {
    private final ConfigHandler configHandler;

    public TweakCooldown(ConfigHandler configHandler) {
        this.configHandler = configHandler;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 0) {
            SpellType spellType = SpellType.fromString(args[0]);
            if (CommandUtils.isSpellOrNotify(sender, spellType, args[0]))
                if (args.length > 1) {
                    try {
                        int input = Integer.parseInt(args[1]);
                        String message = WandsPlugin.PREFIX + spellType.getDisplayName() + "'s cooldown is now default value + " + input;
                        if (CommandUtils.isInRangeOrNotify(sender, input)) {
                            configHandler.setSpellCooldown(spellType, input);
                            sender.sendMessage(message);
                        }
                    } catch (NumberFormatException e) {
                        sender.sendMessage(WandsPlugin.PREFIX + "Invalid number");
                    }
                } else {
                    int cooldown = spellType.behavior.getCooldown();
                    sender.sendMessage(WandsPlugin.PREFIX + "Cooldown of " + spellType.getDisplayName() + " is +" + cooldown + " second" + ((cooldown == 1) ? "" : "s"));
                }
        } else return false;
        return true;
    }
}