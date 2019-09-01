package net.capecraft.member;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.capecraft.Main;

public class KeepInvToggle {
	
	public KeepInvToggle(Player p) {		
		if(p.hasPermission("essentials.keepinv")) {
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + p.getUniqueId().toString() + " permission set essentials.keepinv false");
			p.sendMessage(Main.PREFIX + "Your KeepInventory has been turned off");
		} else {
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + p.getUniqueId().toString() + " permission unset essentials.keepinv");
			p.sendMessage(Main.PREFIX + "Your KeepInventory has been turned on");
		}
	}

}
