package net.capecraft.member;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import net.capecraft.Main;

public class MemberConfig implements Listener {
	
	Plugin plugin;
	File memberFolder;
	
	private static final String alt = "alt";
	private static final String username = "username";
	private static final String playtime = "playtime";
	private static final String jointime = "jointime";
	
	public MemberConfig(Plugin p) {
		plugin = p;
		
		//Makes plugin folders if they don't exist
		if(!p.getDataFolder().exists()) {
			p.getDataFolder().mkdir();			
        }
		
		memberFolder = new File(p.getDataFolder() + "/users/");
		
		if(!memberFolder.exists()) {
			memberFolder.mkdir();
		}
				
	}

	//Gets Play time by loading config and reading it
	public int getPlayTime(String uuid) {		
		File userFile = new File(memberFolder, uuid + ".yml");
		YamlConfiguration userConfig = YamlConfiguration.loadConfiguration(userFile);
		return userConfig.getInt(playtime);
	}
	
	
	//Will update the config depending on line and value
	public void updateConfig(String line, Object value, String uuid) {
		YamlConfiguration userConfig;
		File userFile = new File(memberFolder, uuid + ".yml");
		
		if(!userFile.exists()) {
			userConfig = YamlConfiguration.loadConfiguration(userFile);			
			userConfig.set(playtime, 0);
			userConfig.set(jointime, (System.currentTimeMillis() / 1000));
			userConfig.set(username, 0);
			try {
				userConfig.save(userFile);				
			} catch(IOException e) {
				e.printStackTrace();
			}
		}		
		
		userConfig = YamlConfiguration.loadConfiguration(userFile);
		userConfig.set(line, value);
		try {
			userConfig.save(userFile);				
		} catch(IOException e) {
			e.printStackTrace();
		}		
	}
	
	//Will read config depending on supplied values
	public Object readConfig(String line, String uuid) {		
		File userFile = new File(memberFolder, uuid + ".yml");
		YamlConfiguration userConfig = YamlConfiguration.loadConfiguration(userFile);
				
		return userConfig.get(line);
	}
	
	//Updates user join time, check if they've surpassed 100 hours and don't have member permission
	//and will give them member
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		
		String uuid = p.getUniqueId().toString();

		updateConfig(alt, p.hasPermission("group.alt"), uuid);			
		updateConfig(jointime, (System.currentTimeMillis() / 1000), uuid);
		updateConfig(username, p.getName(), uuid);		
		int playTimeMin = Int.parseInt(readConfig(playtime, uuid));
		
		//25 hours player
		if(playTimeMin >= 1500 && !p.hasPermission("group.player")) {
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + p.getName() + " permission set group.player");
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + p.getName() + " permission unset group.default");
			Bukkit.broadcastMessage(Main.PREFIX + "\ufffd6" + p.getName() + " \ufffdrHas just earned the \ufffdr\ufffd7\ufffdlPLAYER�r rank!");
			plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + p.getName() + " only capecraft:player");
		}

		//100 hours member
		if(playTimeMin >= 6000 && !p.hasPermission("group.member")) {
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + p.getName() + " permission set group.member");
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + p.getName() + " permission unset group.player");
			Bukkit.broadcastMessage(Main.PREFIX + "\ufffd6" + p.getName() + " \ufffdrHas just earned the \ufffdr\ufffdc\ufffdlMEMBER\ufffdr rank!");
			plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + p.getName() + " only capecraft:member");
		}

		//350hr elder
		if(playTimeMin >= 21000 && !p.hasPermission("group.elder")) {
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + p.getName() + " permission set group.elder");
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + p.getName() + " permission unset group.member");
			Bukkit.broadcastMessage(Main.PREFIX + "\ufffd6" + p.getName() + " \ufffdrHas just earned the \ufffdr\ufffd4\ufffdlELDER\ufffdr rank!");
			plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + p.getName() + " only capecraft:elder");
		}
		//1000h Veteran
		if(playTimeMin >= 60000 && !p.hasPermission("group.veteran")) {
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + p.getName() + " permission set group.veteran");
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + p.getName() + " permission unset group.elder");
			Bukkit.broadcastMessage(Main.PREFIX + "\ufffd6" + p.getName() + " �rHas just earned the \ufffdr\ufffd5\ufffdlVETERAN\ufffdr rank!");
			plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + p.getName() + " only capecraft:veteran");
		}	
	}
	
	//Will update play minutes
	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		updatePlayTime(event.getPlayer());
	}
	
	private void updatePlayTime(Player player) {
		String uuid = player.getUniqueId().toString();
		//Playtime in minutes
		int playTimeMin = Integer.parseInt(readConfig(playtime, uuid).toString());
		//Join time unix
		int joinTimeUnix = Integer.parseInt(readConfig(jointime, uuid).toString());
		//difference in seconds
		int joinTimeDiff = (int) ((System.currentTimeMillis() / 1000) - joinTimeUnix);
		//convert to minutes
		joinTimeDiff = joinTimeDiff / 60;
		playTimeMin = playTimeMin + joinTimeDiff;
		
		updateConfig(jointime, (System.currentTimeMillis() / 1000), uuid);
		updateConfig(playtime, playTimeMin, uuid);
	}
	
}
