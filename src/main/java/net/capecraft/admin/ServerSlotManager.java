package net.capecraft.admin;

import java.util.*;
import java.util.logging.Logger;

import net.capecraft.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;



public class ServerSlotManager implements Listener {
	//implements the Minecraft logger
	private Logger log = Bukkit.getLogger();
	//Get current and max players
	private int maxPlayerCount = Bukkit.getServer().getMaxPlayers();
	//Creates afk player queue - mov51
	private Queue<Player> afkQueueList = new LinkedList<>();

	 @EventHandler(priority = EventPriority.HIGHEST)
	 public void onPlayerLoginEvent(PlayerLoginEvent event) {

		 for(Player s : afkQueueList) {//TODO remove
			 log.warning("queue is " + s.getName());//TODO remove
		 }
		 log.warning("queue size is " + afkQueueList.size());//TODO remove
		 //Gets updated player size each tim a player joins - mov51
		 int playerCount = Bukkit.getServer().getOnlinePlayers().size();

		 //Simplifies afk check to 1 bool - mov51
		 boolean doKick = (playerCount >= (maxPlayerCount - 1));

		 log.warning("doKick? " + doKick); //TODO rm
		 log.warning("Size" + afkQueueList.size()); //TODO rm

		 //Checks against the queue size and the doKick condition - mov51
		 if(afkQueueList.size() > 0 && doKick) {
		 	//warns for a kick - mov51
			 log.warning( "attempting to kick" + afkQueueList.poll());
			 //Kicks first active player in queue - mov51
			 Objects.requireNonNull(afkQueueList.poll()).kickPlayer("The server is full!\nWe've had to disconnect your alt to make place for real players");
			 Bukkit.broadcastMessage(Main.PREFIX + "An inactive player has been removed from the playing field to make space! Feel safe in your active minds!");
		 }else if(event.getPlayer().hasPermission("group.alt")) {
			 if (event.getPlayer().hasPermission("group.alt")) {
				 manageAfkQueue(event.getPlayer(), "add");
				 if(doKick) {
				 	//Warns for a blocked join - mov51
					 log.warning( "preventing " + event.getPlayer() + " from joining");
					 //Kicks and broadcasts - mov51
					 event.disallow(Result.KICK_OTHER, "The server is full!\nWe've had to disconnect your alt to make place for real players");
					 Bukkit.broadcastMessage(Main.PREFIX + "An alt has been denied entry! Feel safe in your active minds!");
				 }
			 }
		 }
	 }

	 public void manageAfkQueue(Player p, String mode){
	 	if(mode.equals("add")){
	 		//adds player to end of queue - mov51
	 		afkQueueList.add(p);

	 		log.warning(p.getName() + " added to the queue");//TODO remove

		}else if(mode.equals("del")){
	 		//Loops through the queue while the player is in it - mov51
			while(afkQueueList.contains(p)){
				//Removes the player when ever its found - mov51
				//should only ever run once!! - mov51
				afkQueueList.remove(p);

				log.warning(p.getName() + " removed from the queue");//TODO remove

			}
		 }
	 }

	public static final ServerSlotManager INSTANCE = new ServerSlotManager();
}