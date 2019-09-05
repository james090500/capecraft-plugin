package net.capecraft.admin;

import java.util.LinkedHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import net.capecraft.Main;

public class ServerSlotManager implements Listener {

	private LinkedHashMap<String, Player> afkList = new LinkedHashMap<String, Player>();

	 @EventHandler(priority = EventPriority.HIGHEST)
	 public void onPlayerLoginEvent(PlayerLoginEvent event) {
		 int playerCount = Bukkit.getServer().getOnlinePlayers().size();
		 int maxPlayerCount = Bukkit.getServer().getMaxPlayers();
		 if (playerCount >= (maxPlayerCount - 1)) {

			 //If player joining is an alt and player count 1 from full then disconnect the alt
			 if(event.getPlayer().hasPermission("group.alt")) {
				 event.disallow(Result.KICK_OTHER, "The server is full!\nWe've had to disconnect your alt to make place for real players");
				 Bukkit.broadcastMessage(Main.PREFIX + "An alt has been denied entry! Feel safe in your active minds!");
				 return;
			 } else {
				 kickAfkPlayer();				 				 
			 }
						 
		 }
	 }

	 private void kickAfkPlayer() {
		 if(afkList.size() > 0) {			 
			 String uuid = afkList.entrySet().iterator().next().getKey();
			 afkList.get(uuid).kickPlayer("The server is full!\nWe've had to disconnect your alt to make place for real players");
			 afkList.remove(uuid);
			 Bukkit.broadcastMessage(Main.PREFIX + "An inactive player has been removed from the playing field to make space! Feel safe in your active minds!");
		 }
	 }
	 
	 public void addAfkPlayer(Player p) {
		 String uuid = p.getUniqueId().toString();
		 afkList.put(uuid, p);
	 }
	 
	 public void removeAfkPlayer(Player p) {
		 String uuid = p.getUniqueId().toString();		
		 afkList.remove(uuid);
	 }
	 
	 public static final ServerSlotManager INSTANCE = new ServerSlotManager();

}