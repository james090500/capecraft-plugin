package net.capecraft.commands;

import net.capecraft.Main;
import net.capecraft.events.ServerSlotManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import net.capecraft.events.PlaytimeEventHandler;

public class AfkCommand implements CommandExecutor {

	Plugin plugin;	
	
	public AfkCommand(Plugin p) {
		this.plugin = p;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if(sender instanceof Player){
			if (commandLabel.equalsIgnoreCase("afk")) {
				if(!sender.hasPermission("group.alt")) {
					if(sender.hasPermission("capecraft.playAfk")) {
						int playerCount = Bukkit.getServer().getOnlinePlayers().size();
						if(playerCount <= (ServerSlotManager.INSTANCE.maxPlayerCount - 1)){							
							PlaytimeEventHandler.INSTANCE.setAfk((Player) sender);
							return true;
						}else{
							sender.sendMessage(Main.PREFIX + "You cant afk! The server is too full!");
						}
					}else{
						sender.sendMessage(Main.PREFIX + "You cant afk! If you think this is an error contact a staff member!");
					}
				}else{
					sender.sendMessage(Main.PREFIX + "Your an alt! You are already afk you silly!");
				}
			}
		}
		return false;
	}
	
}
