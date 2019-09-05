package net.capecraft.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.capecraft.Main;

public class Capecommand implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if(sender instanceof Player) {
			if (commandLabel.equalsIgnoreCase("cape")) {
				sender.sendMessage(Main.PREFIX + "To get a cape visit https://minecraftcapes.co.uk/discord");
			}
		}
		return false;
	}
	
}
