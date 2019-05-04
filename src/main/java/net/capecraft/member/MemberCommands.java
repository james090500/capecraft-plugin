package net.capecraft.member;

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

public class MemberCommands implements CommandExecutor {
	
	Plugin plugin;
	
	public MemberCommands(Plugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {		
		//Shows Play time
		if(commandLabel.equalsIgnoreCase("playtime")) {
			if(sender instanceof Player) {
				//Return the user else return current player
				String userPlaytime;
				if(args.length != 0 && !args[0].isEmpty()) {
					userPlaytime = args[0];
				} else {
					userPlaytime = sender.getName();
				}
				
				if(Bukkit.getPlayer(userPlaytime) != null) {						
					MemberConfig memberconfig = new MemberConfig(plugin);
					double playtime = memberconfig.getPlayTime(Bukkit.getPlayer(userPlaytime).getUniqueId().toString());
					playtime = playtime / 60;
					DecimalFormat df = new DecimalFormat("#.##");			
					sender.sendMessage(Main.PREFIX + userPlaytime + " has " + df.format(playtime) + " hours of play time!");
					return true;
				} else {
					sender.sendMessage(Main.PREFIX + "User is not online!");
					return false;
				}
			}
		}
		
		if(commandLabel.equalsIgnoreCase("playtimetop")) {
			HashMap<Integer, String> playtimes = new HashMap<Integer, String>();
			Player p = (Player) sender;
			
			Bukkit.getScheduler().runTask(plugin, new Runnable() {					
				@Override
				public void run() {									
					for(File file : new File(plugin.getDataFolder() + "/users/").listFiles()) {						
						if(file.getName().contains(".yml")) {							
							FileConfiguration conf = YamlConfiguration.loadConfiguration(file);
							if(!conf.getBoolean("alt")) {
								int playtime = conf.getInt("playtime") / 60;
								playtimes.put(playtime, conf.getString("username"));
							}
						}
					}

					int count = 0;
					int max = 4;
					
					p.sendMessage("�e----- �rTop 5 users with playtime �e-----");			
					TreeMap<Integer, String> treemap = new TreeMap<>(playtimes);
					for(Integer entry : treemap.descendingKeySet()) {
						if(treemap.get(entry) != null) {						
							if(count > max) break;
							
							count++;						
							p.sendMessage(count + ". " + entry + "hrs, " + treemap.get(entry));
						}
					}
					p.sendMessage("�e---------------------------------");
				}							
			});	
		}
		return false;
	}
}
