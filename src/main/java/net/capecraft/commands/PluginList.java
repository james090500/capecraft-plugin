package net.capecraft.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import net.capecraft.Main;
import net.capecraft.rantp.RandomTP;

public class PluginList implements CommandExecutor {

	Plugin plugin;	
	
	public PluginList(Plugin p) {
		this.plugin = p;
	}

	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if(commandLabel.equalsIgnoreCase("wild") || commandLabel.equalsIgnoreCase("rtp")) {
			if(sender instanceof Player) {
				new RandomTP().teleportPlayer((Player) sender);
			} else {
				sender.sendMessage(Main.PREFIX + "You are the console");
			}
			return true;
		}
			
		return false;
	}

}
