package net.capecraft.commands;

import net.capecraft.Main;
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
					PlaytimeEventHandler peh = new PlaytimeEventHandler(plugin);
					peh.setAfk((Player) sender);
					return true;
				}else{
					sender.sendMessage(Main.PREFIX + "Your an alt! You are already afk you silly");
				}
			}
		}
		return false;
	}
	
}
