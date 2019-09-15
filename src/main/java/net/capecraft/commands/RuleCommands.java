package net.capecraft.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.capecraft.Main;

public class RuleCommands implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if(sender instanceof Player) {
			if (commandLabel.equalsIgnoreCase("rules")) {
				sender.sendMessage(Main.PREFIX + "https://capecraft.net/rules");
			} else if (commandLabel.equalsIgnoreCase("altrules")) {
				sender.sendMessage(Main.PREFIX + "https://capecraft.net/rules/alts");
			} else if (commandLabel.equalsIgnoreCase("afkrules")) {
				sender.sendMessage(Main.PREFIX + "https://capecraft.net/rules/afk");
			}
		}
		return false;
	}
	
}
