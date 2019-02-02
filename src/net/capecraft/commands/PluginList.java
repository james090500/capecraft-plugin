package net.capecraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import net.capecraft.Main;
import net.capecraft.firework.RandomFirework;
import net.capecraft.rantp.RandomTP;

public class PluginList implements CommandExecutor {

	Plugin plugin;	
	
	public PluginList(Plugin p) {
		this.plugin = p;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if(commandLabel.equalsIgnoreCase("firework")) {
			if(sender.hasPermission("capecraft.firework")) {
				new RandomFirework((Player) sender, false);
				Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
					@Override
					public void run() {
						new RandomFirework((Player) sender, false);
					}				
				}, 10);				
				Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
					@Override
					public void run() {
						new RandomFirework((Player) sender, false);
					}				
				}, 20);
			} else {
				sender.sendMessage(Main.PREFIX + "This is a donator perk only. You can get it with /donate");
			}
		} else if(commandLabel.equalsIgnoreCase("wild") || commandLabel.equalsIgnoreCase("rtp")) {
			if(sender instanceof Player) {
				RandomTP rtp = new RandomTP();
				rtp.teleportPlayer((Player) sender);
			} else {
				sender.sendMessage(Main.PREFIX + "You are the console");
			}
			return true;
		}
			
		return false;
	}

}
