package net.capecraft.commands;

import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import net.capecraft.Main;
import net.capecraft.events.PlaytimeEventHandler;

public class MemberCommands implements CommandExecutor {

	Plugin plugin;

	public MemberCommands(Plugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		// Shows Play time
		if(sender instanceof Player) {
			if (commandLabel.equalsIgnoreCase("playtime")) {			
				this.getPlaytimeMessage(sender, args);			
			}
		
			if (commandLabel.equalsIgnoreCase("playtimetop")) {
				this.getTopPlaytime(sender);
			}
		}
		return false;
	}
		
	private void getPlaytimeMessage(CommandSender sender, String[] args) {
		String userPlaytime = (args.length != 0 && !args[0].isEmpty()) ? args[0] : sender.getName();		

		//If player is online
		if (Bukkit.getPlayer(userPlaytime) != null) {
			//Get Playtime from config			
			Player p = Bukkit.getPlayer(userPlaytime);
			PlaytimeEventHandler.INSTANCE.updatePlayTime(p);
			
			//Make playtime readable
			double playtime = PlaytimeEventHandler.INSTANCE.getPlayTime(p);
			playtime = playtime / 60;
			DecimalFormat df = new DecimalFormat("#.##");
			
			//Show playtime
			sender.sendMessage(Main.PREFIX + userPlaytime + " has " + df.format(playtime) + " hours of play time!");			
		} else {
			sender.sendMessage(Main.PREFIX + "User is not online!");			
		}
	}

	
	private void getTopPlaytime(CommandSender sender) {
		HashMap<Integer, String> playtimes = new HashMap<Integer, String>();
		Player p = (Player) sender;

		Bukkit.getScheduler().runTask(plugin, new Runnable() {
			@Override
			public void run() {
				//Load all files from config and store playtimes in a hashmap
				for (File file : new File(plugin.getDataFolder() + "/users/").listFiles()) {
					if (file.getName().contains(".yml")) {
						FileConfiguration conf = YamlConfiguration.loadConfiguration(file);
						if (!conf.getBoolean("alt")) {
							int playtime = conf.getInt("playtime") / 60;
							playtimes.put(playtime, conf.getString("username"));
						}
					}
				}

				int count = 0;
				int max = 10;

				//Sorts hashmap and sends players top players
				p.sendMessage("§e-----§r Top "+max+" users with playtime §e-----");
				TreeMap<Integer, String> treemap = new TreeMap<>(playtimes);
				for (Integer entry : treemap.descendingKeySet()) {
					if (treemap.get(entry) != null) {
						if (count > (max - 1)) {
							break;
						}

						count++;
						p.sendMessage(count + ". " + entry + "hrs, " + treemap.get(entry));
					}
				}
				p.sendMessage("§e---------------------------------");
			}
		});
	}
}
