package net.capecraft.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.capecraft.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public class ServerSlotManager implements Listener {

	//Get max players
	private  int maxPlayerCount = Bukkit.getServer().getMaxPlayers();

	private Player[] afkList = new Player[maxPlayerCount];
	private int addPos = 0;

	 @EventHandler(priority = EventPriority.HIGHEST)
	 public void onPlayerLoginEvent(PlayerLoginEvent event) {
		 afkListEdit(event.getPlayer(), "add");

		 int playerCount = Bukkit.getServer().getOnlinePlayers().size();


		 if (playerCount >= (maxPlayerCount - 1)) {

			 //If player joining is an alt and player count 1 from full then disconnect the alt
			 if(event.getPlayer().hasPermission("group.alt")) {
				 event.disallow(Result.KICK_OTHER, "The server is full!\nWe've had to disconnect your alt to make place for real players");
				 Bukkit.broadcastMessage(Main.PREFIX + "An alt has been denied entry! Feel safe in your active minds!");
				 return;
			 }
			 checkForKick(playerCount);
		 }
		 //Create a temp list for online alts

	 }

	 public void afkListEdit(Player p, String mode){
	 	//TODO add players to list
		 //player
		 //add or remove?

		 if (mode.equals("add")){
		 	afkList[addPos] = p;
		 	if (addPos  > maxPlayerCount){
		 		maxPlayerCount = 0;
			}
		 } else if(mode.equals("del")){
			 int i;
		 	for (i = 0; i < afkList.length; i++){
		 		 if(afkList[i] != null){
					afkList[i] = null;
				 }
			}
		 }

	 }

	 public void checkForKick(int playerCount){
		 //See if player count is 1 less than max when a player joins

		 	Player[] smallAfkList = new Player[getLength(afkList)];
			 int i;
			 int x = 0;
		 	for (i = 0; i < afkList.length; i++){
		 		if(afkList[i] != null){
		 			smallAfkList[x] = afkList[i];
		 			x++;
				}
			}

			 if(smallAfkList.length > 0) {
				 //Get a random alt online and kick them
				 Player nextAfk = smallAfkList[0];
				 nextAfk.kickPlayer("The server is full!\nWe've had to disconnect your alt to make place for real players");
				 Bukkit.broadcastMessage(Main.PREFIX + "An inactive player has been removed from the playing field to make space! Feel safe in your active minds!");
			 }
	}

	public static <T> int getLength(T[] arr){
		int count = 0;
		for(T el : arr)
			if (el != null)
				++count;
		return count;
	}

}