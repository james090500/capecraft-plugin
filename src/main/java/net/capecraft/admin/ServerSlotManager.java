package net.capecraft.admin;

import java.util.*;

import net.capecraft.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public class ServerSlotManager implements Listener {

	//Get current and max players
	private int playerCount = Bukkit.getServer().getOnlinePlayers().size();
	Queue<Player> afkQueueList = new LinkedList<>();

	 @EventHandler(priority = EventPriority.HIGHEST)
	 public void onPlayerLoginEvent(PlayerLoginEvent event) {

		 int maxPlayerCount = Bukkit.getServer().getMaxPlayers();
		 
		 //See if player count is 1 less than max when a player joins
		 if (playerCount >= (maxPlayerCount - 1)) {

			 //If player joining is an alt and player count 1 from full then disconnect the alt			 
			 if(event.getPlayer().hasPermission("group.alt")) {				 
				 event.disallow(Result.KICK_OTHER, "The server is full!\nWe've had to disconnect your alt to make place for real players");
				 Bukkit.broadcastMessage(Main.PREFIX + "An alt has been denied entry! Feel safe in your active minds!");
				 return;
			 }
			 
			 //If there are alts online
			 if(afkQueueList.size() > 0) {
				 afkQueueList.poll().kickPlayer("The server is full!\nWe've had to disconnect your alt to make place for real players");
				 Bukkit.broadcastMessage(Main.PREFIX + "An inactive player has been removed from the playing field to make space! Feel safe in your active minds!");
			 }
		 }
	 }

	 public void manageAfkQueue(Player p, String mode){
	 	if(mode.equals("add")){
	 		afkQueueList.add((p));
		}else if(mode.equals("del")){
			while(afkQueueList.contains(p)){
				afkQueueList.remove(p);
			}
		 }
	 }
}