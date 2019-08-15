package net.capecraft.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class ServerSlotManager implements Listener {

	 @EventHandler(priority = EventPriority.HIGHEST)
	 public void onPlayerLoginEvent(PlayerLoginEvent event) {
		 //Get current and max players
		 int playerCount = Bukkit.getServer().getOnlinePlayers().size();
		 int maxPlayerCount = Bukkit.getServer().getMaxPlayers();
		 //Create a temp list for online alts
		 List<Player> altsOnline = new ArrayList<Player>();
		 
		 //See if player count is 1 less than max when a player joins
		 if (playerCount == (maxPlayerCount - 1)) {
			 //If player joining is an alt and player count 1 from full then disconnect the alt
			 if(event.getPlayer().hasPermission("group.alt")) {
				 event.getPlayer().kickPlayer("The server is full!\nWe've had to disconnect your alt to make place for real players");
				 return;
			 }
			 
			 //If player count is 1 less than max, loop through alts and add them to altsOnline
			 Bukkit.getOnlinePlayers().stream().filter(p -> p.hasPermission("group.alt")).forEach(p -> altsOnline.add(p));
			 
			 //Get a random alt online and kick them
			 Random rand = new Random();
			 Player randomAlt = altsOnline.get(rand.nextInt(altsOnline.size()));
			 randomAlt.kickPlayer("The server is full!\nWe've had to disconnect your alt to make place for real players");
			 return;
		 }
	 }
}