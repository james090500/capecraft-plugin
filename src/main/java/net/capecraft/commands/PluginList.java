package net.capecraft.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import net.capecraft.Main;
import net.capecraft.member.KeepInvToggle;
import net.capecraft.member.MemberConfig;
import net.capecraft.rantp.RandomTP;

public class PluginList implements CommandExecutor {

	Plugin plugin;	
	
	public PluginList(Plugin p) {
		this.plugin = p;
	}

	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if(sender instanceof Player) {
			if(commandLabel.equalsIgnoreCase("wild") || commandLabel.equalsIgnoreCase("rtp")) {			
				new RandomTP().teleportPlayer((Player) sender);
				return true;
			} else if(commandLabel.equalsIgnoreCase("keepinv")) {
				new KeepInvToggle((Player) sender);
				return true;
			} else if (commandLabel.equalsIgnoreCase("cape")) {
				sender.sendMessage(Main.PREFIX + "To get a cape visit https://minecraftcapes.co.uk/discord");
			} else if (commandLabel.equalsIgnoreCase("afk")) {
				MemberConfig memberconfig = new MemberConfig(plugin);									
				memberconfig.setAfk((Player) sender);
				return true;
			}			
		} else {
			sender.sendMessage(Main.PREFIX + "You are the console");			
		}
		return false;				
	}

}
