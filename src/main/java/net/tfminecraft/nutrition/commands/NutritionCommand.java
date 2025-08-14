package net.tfminecraft.nutrition.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.tfminecraft.nutrition.data.PlayerData;
import net.tfminecraft.nutrition.manager.NutritionManager;

public class NutritionCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        PlayerData data = NutritionManager.get(p);
        data.info(false); // Force feedback message with bars
        return true;
    }
}
