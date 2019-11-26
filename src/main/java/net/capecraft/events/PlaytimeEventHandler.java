package net.capecraft.events;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import net.capecraft.Main;
import net.capecraft.events.comSpy.ComSpy;

public class PlaytimeEventHandler implements Listener {

	//Some key variables
	Plugin plugin;
	File memberFolder;

	//Config loaded in memory to reduce IO
	private HashMap<String, YamlConfiguration> playerConfigs = new HashMap<>();

	//config enums i guess
	public static final String alt = "alt";
	public static final String afk = "isAfk";
	public static final String username = "username";
	public static final String playtime = "playtime";
	public static final String jointime = "jointime";
	public static final String isSpying = "isSpying";

	//Instance for reduced memory load
	public static PlaytimeEventHandler INSTANCE;

	/**
	 * Constructor, sets basic variables such as folders and the instance
	 * @param pluginInstance The plugin instance
	 */
	public PlaytimeEventHandler(Plugin pluginInstance) {
		this.plugin = pluginInstance;

		//Makes plugin folders if they don't exist
		if(!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdir();
        }

		memberFolder = new File(plugin.getDataFolder() + "/users/");

		if(!memberFolder.exists()) {
			memberFolder.mkdir();
		}

		INSTANCE = this;
		
	}

	/**
	 * Returns a users play time
	 * @param player The Player Object
	 * @return
	 */
	public int getPlayTime(Player player) {
		//Update the playtime first
		updatePlayTime(player);

		//Now return playtime		
		return (int) readConfig(playtime, player.getUniqueId().toString());		
	}


	/**
	 * Updates the config in memory and saves to IO
	 * @param line The line of the config 
	 * @param value The value to change
	 * @param uuid The uuid of the config
	 */
	public void updateConfig(String line, Object value, String uuid) {
		File userFile = new File(memberFolder, uuid + ".yml");
		
		if(playerConfigs.get(uuid) != null) {
			playerConfigs.get(uuid).set(line, value);
			try {
				playerConfigs.get(uuid).save(userFile);
			} catch(IOException e) {
				e.printStackTrace();
			}
		} else {
			YamlConfiguration userConfig;			

			if(!userFile.exists()) {
				userConfig = YamlConfiguration.loadConfiguration(userFile);
				userConfig.set(playtime, 0);
				userConfig.set(jointime, (System.currentTimeMillis() / 1000));
				userConfig.set(username, 0);
				userConfig.set(afk, "false");				
				try {
					userConfig.save(userFile);
				} catch(IOException e) {
					e.printStackTrace();
				}
			}

			userConfig = YamlConfiguration.loadConfiguration(userFile);
			userConfig.set(line, value);
			playerConfigs.put(uuid, userConfig);
			try {
				userConfig.save(userFile);
			} catch(IOException e) {
				e.printStackTrace();
			}	
		}
	}

	/**
	 * Read config in to memory if not already loaded. Else it will read from memory
	 * @param line The line of the config 
	 * @param uuid The uuid to change
	 * @return Value from config
	 */
	public Object readConfig(String line, String uuid) {		
		if(playerConfigs.get(uuid) != null) {			
			return playerConfigs.get(uuid).get(line);			
		} else {			
			File userFile = new File(memberFolder, uuid + ".yml");
			YamlConfiguration userConfig = YamlConfiguration.loadConfiguration(userFile);
			playerConfigs.put(uuid, userConfig);
			return userConfig.get(line);
		}
	}

	/**
	 * Resets configs to normal. Add alts to queues. And gives playtime ranks
	 */
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		String uuid = player.getUniqueId().toString();

		updateConfig(alt, player.hasPermission("group.alt"), uuid);
		updateConfig(jointime, (System.currentTimeMillis() / 1000), uuid);
		updateConfig(username, player.getName(), uuid);		
		updateConfig(afk, false, uuid);
		
		Object isStaffSpying = readConfig(isSpying, uuid);
		if(isStaffSpying != null) {
			if(player.hasPermission("capecraft.comspy")) {
				if((Boolean) readConfig(isSpying, uuid)) {
					ComSpy.INSTANCE.addComListener(player);
				}
			} else {
				updateConfig(isSpying, null, uuid);
			}
		}
		
		int playTimeMin = Integer.parseInt(readConfig(playtime, uuid).toString());

		if(player.hasPermission("group.alt")) {
			ServerSlotManager.INSTANCE.addAfkPlayer(player);
			return;
		}
		
		//25 hours regular
		if(playTimeMin >= 1500 && !player.hasPermission("group.regular")) {
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + player.getUniqueId().toString() + " permission set group.regular");
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + player.getUniqueId().toString() + " permission unset group.default");
			Bukkit.broadcastMessage(Main.PREFIX + "§6" + player.getName() + " §rHas just earned the §r§7§lREGULAR§r rank!");
			plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + player.getName() + " only capecraft:regular");
		}

		//100 hours player
		if(playTimeMin >= 6000 && !player.hasPermission("group.player")) {
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + player.getUniqueId().toString() + " permission set group.player");
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + player.getUniqueId().toString() + " permission unset group.regular");
			Bukkit.broadcastMessage(Main.PREFIX + "§6" + player.getName() + " §rHas just earned the §r§f§lPLAYER§r rank!");
			plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + player.getName() + " only capecraft:player");
		}

		//200 hours member
		if(playTimeMin >= 12000 && !player.hasPermission("group.member")) {
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + player.getUniqueId().toString() + " permission set group.member");
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + player.getUniqueId().toString() + " permission unset group.player");
			Bukkit.broadcastMessage(Main.PREFIX + "§6" + player.getName() + " §rHas just earned the §r§c§lMEMBER§r rank!");
			plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + player.getName() + " only capecraft:member");
		}

		//350hr elder
		if(playTimeMin >= 21000 && !player.hasPermission("group.elder")) {
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + player.getUniqueId().toString() + " permission set group.elder");
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + player.getUniqueId().toString() + " permission unset group.member");
			Bukkit.broadcastMessage(Main.PREFIX + "§6" + player.getName() + " §rHas just earned the §r§4§lELDER§r rank!");
			plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + player.getName() + " only capecraft:elder");
		}

		//700h Veteran
		if(playTimeMin >= 42000 && !player.hasPermission("group.veteran")) {
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + player.getUniqueId().toString() + " permission set group.veteran");
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + player.getUniqueId().toString() + " permission unset group.elder");
			Bukkit.broadcastMessage(Main.PREFIX + "§6" + player.getName() + " §rHas just earned the §r§5§lVETERAN§r rank!");
			plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + player.getName() + " only capecraft:veteran");
		}

		//1000h Legend
		if(playTimeMin >= 60000 && !player.hasPermission("group.legend")) {
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + player.getUniqueId().toString() + " permission set group.legend");
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + player.getUniqueId().toString() + " permission unset group.veteran");
			Bukkit.broadcastMessage(Main.PREFIX + "§6" + player.getName() + " §rHas just earned the §r§e§lLEGEND§r rank!");
			plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + player.getName() + " only capecraft:legend");
		}
	}

	/**
	 * Updates player config and removes from memory on leave
	 */
	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		//Removes player from queue when they leave to prevent npe when the server tries to kick them - mov51
		ServerSlotManager.INSTANCE.removeAfkPlayer(event.getPlayer());
		updatePlayTime(event.getPlayer());
		playerConfigs.remove(event.getPlayer().getUniqueId().toString());
	}

	/**
	 * Updates the play time
	 * @param player The Player Object
	 */
	public void updatePlayTime(Player player) {
		//gets UUID to string
		String uuid = player.getUniqueId().toString();
		//gets isAfk from the playerdata file		
		if(!Boolean.parseBoolean(readConfig(afk, uuid).toString())){
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

	/**
	 * Sets player AFK and updates config
	 * @param player The Player Object
	 */
	public void setAfk(Player player) {
		//gets UUID to string - mov51
		String uuid = player.getUniqueId().toString();
		//gets isAfk from the playerdata file /Sets isAfk to the desired result (opposite)
		boolean isAfk = Boolean.parseBoolean(readConfig(afk, uuid).toString());

		//Checks for the opposite of afk, if true removes player if false adds them - mov51
		if(isAfk) {
			//If isAfk is true, removes them from the afk queue
			player.sendMessage(Main.PREFIX + "You're no longer AFK");
			ServerSlotManager.INSTANCE.removeAfkPlayer(player);
		} else {
			//If isAfk is false, adds them to the afk queue
			player.sendMessage(Main.PREFIX + "You're now AFK. Make sure to follow the AFK Rules");
			ServerSlotManager.INSTANCE.addAfkPlayer(player);
		}
		//updates joinTime - mov51
		updateConfig(jointime, (System.currentTimeMillis() / 1000), uuid);
		//updates config with new isAfk state (opposite of original state) - mov51
		updateConfig(afk, !isAfk, uuid);
	}
}
