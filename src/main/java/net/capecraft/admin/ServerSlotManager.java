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

	public Logger log = Bukkit.getLogger();

	//Get current and max players
	private int maxPlayerCount = Bukkit.getServer().getMaxPlayers();
	private Queue<Player> afkQueueList = new LinkedList<>();

	 @EventHandler(priority = EventPriority.HIGHEST)
	 public void onPlayerLoginEvent(PlayerLoginEvent event) {

		 for(Player s : afkQueueList) {//TODO remove
			 log.warning("queue is " + s.getName());//TODO remove
		 }
		 log.warning("queue size is " + afkQueueList.size());//TODO remove
		 int playerCount = Bukkit.getServer().getOnlinePlayers().size();

		 boolean doKick = (playerCount >= (maxPlayerCount - 1));
		 log.warning("doKick? " + doKick); //TODO rm
		 log.warning("Size" + afkQueueList.size()); //TODO rm
		 if(afkQueueList.size() > 0 && doKick) {
			 log.warning( "attempting to kick" + afkQueueList.poll());
			 Objects.requireNonNull(afkQueueList.poll()).kickPlayer("The server is full!\nWe've had to disconnect your alt to make place for real players");
			 Bukkit.broadcastMessage(Main.PREFIX + "An inactive player has been removed from the playing field to make space! Feel safe in your active minds!");
		 }else if(event.getPlayer().hasPermission("group.alt")) {
			 if (event.getPlayer().hasPermission("group.alt")) {
				 manageAfkQueue(event.getPlayer(), "add");
				 if(doKick) {
					 log.warning( "preventing " + event.getPlayer() + " from joining");
					 event.disallow(Result.KICK_OTHER, "The server is full!\nWe've had to disconnect your alt to make place for real players");
					 Bukkit.broadcastMessage(Main.PREFIX + "An alt has been denied entry! Feel safe in your active minds!");
					 return;
				 }
			 }
		 }
	 }

	 public void manageAfkQueue(Player p, String mode){
	 	if(mode.equals("add")){
	 		afkQueueList.add(p);
	 		log.warning(p.getName() + " added to the queue");//TODO remove
		}else if(mode.equals("del")){
			while(afkQueueList.contains(p)){
				afkQueueList.remove(p);
				log.warning(p.getName() + " removed from the queue");//TODO remove
			}
		 }
	 }

	public static final ServerSlotManager INSTANCE = new ServerSlotManager();
}