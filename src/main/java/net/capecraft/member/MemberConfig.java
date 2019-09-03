package net.capecraft.member;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

import net.capecraft.admin.ServerSlotManager;
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

	private HashMap<String, YamlConfiguration> playerConfigs = new HashMap<>();

	private static final String alt = "alt";
	private static final String afk = "isAfk";
	private static final String username = "username";
	private static final String playtime = "playtime";
	private static final String jointime = "jointime";

	public Logger log = Bukkit.getLogger();

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
	public int getPlayTime(Player p) {
		//Update the playtime first
		updatePlayTime(p);

		//Now return playtime
		File userFile = new File(memberFolder, p.getUniqueId().toString() + ".yml");
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

	//Will read config depending on supplied values
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

	//Updates user join time, check if they've surpassed 100 hours and don't have member permission
	//and will give them member
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();

		String uuid = p.getUniqueId().toString();

		updateConfig(alt, p.hasPermission("group.alt"), uuid);
		updateConfig(jointime, (System.currentTimeMillis() / 1000), uuid);
		updateConfig(username, p.getName(), uuid);
		updateConfig(afk, "false", uuid);
		int playTimeMin = Integer.parseInt(readConfig(playtime, uuid).toString());

		if(p.hasPermission("group.alt")) {
			return;
		}

		//25 hours regular
		if(playTimeMin >= 1500 && !p.hasPermission("group.regular")) {
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + p.getUniqueId().toString() + " permission set group.regular");
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + p.getUniqueId().toString() + " permission unset group.default");
			Bukkit.broadcastMessage(Main.PREFIX + "§6" + p.getName() + " §rHas just earned the §r§7§lREGULAR§r rank!");
			plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + p.getName() + " only capecraft:regular");
		}

		//100 hours player
		if(playTimeMin >= 6000 && !p.hasPermission("group.player")) {
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + p.getUniqueId().toString() + " permission set group.player");
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + p.getUniqueId().toString() + " permission unset group.regular");
			Bukkit.broadcastMessage(Main.PREFIX + "§6" + p.getName() + " §rHas just earned the §r§f§lPLAYER§r rank!");
			plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + p.getName() + " only capecraft:player");
		}

		//200 hours member
		if(playTimeMin >= 12000 && !p.hasPermission("group.member")) {
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + p.getUniqueId().toString() + " permission set group.member");
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + p.getUniqueId().toString() + " permission unset group.player");
			Bukkit.broadcastMessage(Main.PREFIX + "§6" + p.getName() + " §rHas just earned the §r§c§lMEMBER§r rank!");
			plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + p.getName() + " only capecraft:member");
		}

		//350hr elder
		if(playTimeMin >= 21000 && !p.hasPermission("group.elder")) {
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + p.getUniqueId().toString() + " permission set group.elder");
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + p.getUniqueId().toString() + " permission unset group.member");
			Bukkit.broadcastMessage(Main.PREFIX + "§6" + p.getName() + " §rHas just earned the §r§4§lELDER§r rank!");
			plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + p.getName() + " only capecraft:elder");
		}

		//700h Veteran
		if(playTimeMin >= 42000 && !p.hasPermission("group.veteran")) {
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + p.getUniqueId().toString() + " permission set group.veteran");
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + p.getUniqueId().toString() + " permission unset group.elder");
			Bukkit.broadcastMessage(Main.PREFIX + "§6" + p.getName() + " §rHas just earned the §r§5§lVETERAN§r rank!");
			plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + p.getName() + " only capecraft:veteran");
		}

		//1000h Veteran
		if(playTimeMin >= 60000 && !p.hasPermission("group.legend")) {
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + p.getUniqueId().toString() + " permission set group.legend");
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + p.getUniqueId().toString() + " permission unset group.veteran");
			Bukkit.broadcastMessage(Main.PREFIX + "§6" + p.getName() + " §rHas just earned the §r§6§lVETERAN§r rank!");
			plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), "advancement grant " + p.getName() + " only capecraft:legend");
		}
	}

	//Will update play minutes
	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		//Removes player from queue when they leave to prevent npe when the server tries to kick them - mov51
		ServerSlotManager.INSTANCE.manageAfkQueue(event.getPlayer(), "del");
		log.warning("MemberConf removed " + event.getPlayer());//todo remove!
		updatePlayTime(event.getPlayer());
		playerConfigs.remove(event.getPlayer().getUniqueId().toString());
	}

	public void updatePlayTime(Player player) {
		//gets UUID to string
		String uuid = player.getUniqueId().toString();
		//gets isAfk from the playerdata file
		boolean isAfk = Boolean.parseBoolean(readConfig(afk, uuid).toString());
		if(!isAfk){
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

	public void setAfk(Player player) {
		//gets UUID to string - mov51
		String uuid = player.getUniqueId().toString();
		//gets isAfk from the playerdata file /Sets isAfk to the desired result (opposite)
		boolean isAfk = Boolean.parseBoolean(readConfig(afk, uuid).toString());

		//Checks for the opposite of afk, if true removes player if false adds them - mov51
		if(!isAfk){
			//If isAfk is false, adds them to the afk queue for prep
			ServerSlotManager.INSTANCE.manageAfkQueue(player, "add");
			log.warning("MemberConf added " + player);//todo remove
		}else{
			//If isAfk is true, removes them from the afk queue for prep
			ServerSlotManager.INSTANCE.manageAfkQueue(player, "del");
			log.warning("MemberConf removed " + player);//todo remove
		}
			//updates joinTime - mov51
			updateConfig(jointime, (System.currentTimeMillis() / 1000), uuid);
			//updates config with new isAfk state (opposite of original state) - mov51
			updateConfig(afk, !isAfk, uuid);
		}

	}
