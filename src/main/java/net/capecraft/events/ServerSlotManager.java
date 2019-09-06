package net.capecraft.events;

import java.util.LinkedList;
import java.util.Queue;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import net.capecraft.Main;

public class ServerSlotManager implements Listener {
	//Get the max player count on initialization - mov51
	private int maxPlayerCount = Bukkit.getServer().getMaxPlayers();
	//Create a player queue initialized with a linked list - mov51
	private Queue<Player> afkQueueList = new LinkedList<>();

	/**
	 * Will listen for player login and pefrom slot checks
	 * @param event
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	 public void onPlayerLoginEvent(PlayerLoginEvent event) {
		//Updates the player count with each check - mov51
		 int playerCount = Bukkit.getServer().getOnlinePlayers().size();
		 //Turns the kick condition into a bool - mov51
		 boolean doKick = (playerCount >= (maxPlayerCount - 1));
		 if(ServerSlotManager.INSTANCE.afkQueueList.size() > 0 && doKick && event.getPlayer().hasPermission("group.alt")) {
		 	//Prevents alts from joining - mov51
			 //Provide a message for alts - mov51
			 event.disallow(Result.KICK_OTHER, "The server is full!\nWe've had to disconnect your alt to make place for real players");
			 //Broadcast ensuring users that the server is protecting them - mov51
			 Bukkit.broadcastMessage(Main.PREFIX + "An alt has been denied entry! Feel safe in your active minds!");
		 } else {
		 	//Run the checkAfk method if all else fails - mov51
			 checkAfk();
		 }

	 }

	/**
	 * Will check if the anyone from the AFK queue can be kicked to allow real players
	 */
	 public void checkAfk(){
		 //Updates the player count with each check - mov51
		 int playerCount = Bukkit.getServer().getOnlinePlayers().size();
		 //Turns the kick condition into a bool - mov51
		 boolean doKick = (playerCount >= (maxPlayerCount - 1));
		 //Checks against the queue size and doKick condition - mov51
		 if(ServerSlotManager.INSTANCE.afkQueueList.size() > 0 && doKick) {
		 	//Kicks next player in queue (FIFO) - mov51
			 ServerSlotManager.INSTANCE.afkQueueList.poll().kickPlayer("The server is full!\nWe've had to disconnect your alt to make place for real players");
			 //Broadcast ensuring users that the server is protecting them - mov51
			 Bukkit.broadcastMessage(Main.PREFIX + "An inactive player has been removed from the playing field to make space! Feel safe in your active minds!");
		 }		 
	 }
	 
	 /**
	  * Will add a player to the AFK Kick queue
	  * @param p The AFK Player
	  */
	 public void addAfkPlayer(Player p) {
		//Adds player to the queue's singleton instance - mov51
		 ServerSlotManager.INSTANCE.afkQueueList.add(p);
		 //Sets the kickexempt permission to true - mov51 //TODO luckPerms integration
		 Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + p.getUniqueId().toString() + " permission set essentials.afk.kickexempt true");
	 }
	 
	 /**
	  * Will remove a player from the AFK Kick queue
	  * @param p The active player
	  */
	 public void removeAfkPlayer(Player p) {
		 //Removes player from the queue's singleton instance - mov51
		 ServerSlotManager.INSTANCE.afkQueueList.remove(p);
		 //Sets the kickexempt permission to false - mov51
		 Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + p.getUniqueId().toString() + " permission set essentials.afk.kickexempt false");
	 }	 

	 //Access singleton instance - mov51
    public ServerSlotManager(){}
	//Create singleton instance - mov51
	public static final ServerSlotManager INSTANCE = new ServerSlotManager();
}