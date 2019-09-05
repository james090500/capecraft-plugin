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

		 //Gets updated player size each tim a player joins - mov51
		 int playerCount = Bukkit.getServer().getOnlinePlayers().size();

		 //Simplifies afk check to 1 bool - mov51
		 boolean doKick = (playerCount >= (maxPlayerCount - 1));

		 //Checks against the queue size, doKick condition, and alt rank for alt prevention - mov51
		 if(ServerSlotManager.INSTANCE.afkQueueList.size() > 0 && doKick && event.getPlayer().hasPermission("group.alt")) {
				 	//Warns for a blocked join - mov51
					 log.warning( "preventing " + event.getPlayer() + " from joining");
					 //Kicks and broadcasts - mov51
					 event.disallow(Result.KICK_OTHER, "The server is full!\nWe've had to disconnect your alt to make place for real players");
					 Bukkit.broadcastMessage(Main.PREFIX + "An alt has been denied entry! Feel safe in your active minds!");
				 }else{
			 checkAfk();
		 }

		 }


	 public void checkAfk(){
		 //Gets updated player size each tim a player joins - mov51
		 int playerCount = Bukkit.getServer().getOnlinePlayers().size();

		 //Simplifies afk check to 1 bool - mov51
		 boolean doKick = (playerCount >= (maxPlayerCount - 1));

		 //Checks against the queue size and the doKick condition - mov51
		 if(ServerSlotManager.INSTANCE.afkQueueList.size() > 0 && doKick) {
			 //warns for a kick - mov51
			 log.warning( "attempting to kick" + afkQueueList.poll());
			 //Kicks first active player in queue - mov51
			 Objects.requireNonNull(ServerSlotManager.INSTANCE.afkQueueList.poll()).kickPlayer("The server is full!\nWe've had to disconnect your alt to make place for real players");
			 Bukkit.broadcastMessage(Main.PREFIX + "An inactive player has been removed from the playing field to make space! Feel safe in your active minds!");
			 }
	 }

	 public void manageAfkQueue(Player p, String mode){
	 	if(mode.equals("add")){
	 		//adds player to end of queue - mov51
            ServerSlotManager.INSTANCE.afkQueueList.add(p);
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + p.getUniqueId().toString() + " permission set essentials.afk.kickexempt true");

		}else if(mode.equals("del")){
	 		//Loops through the queue while the player is in it - mov51
			while(ServerSlotManager.INSTANCE.afkQueueList.contains(p)){
				//Removes the player when ever its found - mov51
				//should only ever run once!! - mov51
                ServerSlotManager.INSTANCE.afkQueueList.remove(p);
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + p.getUniqueId().toString() + " permission set essentials.afk.kickexempt false");

			}
		 }
	 }

    public ServerSlotManager(){}

	public static final ServerSlotManager INSTANCE = new ServerSlotManager();
}