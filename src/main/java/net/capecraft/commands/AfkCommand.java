package net.capecraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.capecraft.Main;
import net.capecraft.events.PlaytimeEventHandler;
import net.capecraft.events.ServerSlotManager;

public class AfkCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		//Checks sender is playr
		if(sender instanceof Player) {
			//Make sure command is /afk
			if (commandLabel.equalsIgnoreCase("afk")) {
				
				//Check if player is NOT an alt
				if(sender.hasPermission("group.alt")) {
					sender.sendMessage(Main.PREFIX + "Your an alt! You are already AFK you silly!");
					return false;
				}
								
				//Make sure player has afk permissions
				if(sender.hasPermission("capecraft.playafk")) {
					//Make sure there's enough slots to do /afk
					int playerCount = Bukkit.getServer().getOnlinePlayers().size();
					if(playerCount <= (ServerSlotManager.INSTANCE.maxPlayerCount - 1)) {							
						PlaytimeEventHandler.INSTANCE.setAfk((Player) sender);						
						return true;
					} else {
						sender.sendMessage(Main.PREFIX + "You cant AFK! The server is too full!");
					}
				} else {
					sender.sendMessage(Main.PREFIX + "You cant AFK! If you think this is an error contact a staff member!");				
				}
			}
		}
		return false;
	}
	
}
