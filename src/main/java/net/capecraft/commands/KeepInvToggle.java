package net.capecraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.capecraft.Main;

public class KeepInvToggle implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if(sender instanceof Player) {
			if(commandLabel.equalsIgnoreCase("keepinv")) {
				this.toggleInv((Player) sender);
				return true;
			}
		}
		return false;
	}
	
	private void toggleInv(Player p) {		
		if(p.hasPermission("essentials.keepinv")) {
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + p.getUniqueId().toString() + " permission set essentials.keepinv false");
			p.sendMessage(Main.PREFIX + "Your KeepInventory has been turned off");
		} else {
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + p.getUniqueId().toString() + " permission unset essentials.keepinv");
			p.sendMessage(Main.PREFIX + "Your KeepInventory has been turned on");
		}
	}

}
