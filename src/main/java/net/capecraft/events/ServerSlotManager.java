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

	private int maxPlayerCount = Bukkit.getServer().getMaxPlayers();
	private Queue<Player> afkQueueList = new LinkedList<>();

	@EventHandler(priority = EventPriority.HIGHEST)
	 public void onPlayerLoginEvent(PlayerLoginEvent event) {
		 int playerCount = Bukkit.getServer().getOnlinePlayers().size();
		 boolean doKick = (playerCount >= (maxPlayerCount - 1));
		 if(ServerSlotManager.INSTANCE.afkQueueList.size() > 0 && doKick && event.getPlayer().hasPermission("group.alt")) {
			 event.disallow(Result.KICK_OTHER, "The server is full!\nWe've had to disconnect your alt to make place for real players");
			 Bukkit.broadcastMessage(Main.PREFIX + "An alt has been denied entry! Feel safe in your active minds!");
		 } else {
			 checkAfk();
		 }

	 }

	 public void checkAfk(){
		 int playerCount = Bukkit.getServer().getOnlinePlayers().size();

		 boolean doKick = (playerCount >= (maxPlayerCount - 1));

		 if(ServerSlotManager.INSTANCE.afkQueueList.size() > 0 && doKick) {			 
			 ServerSlotManager.INSTANCE.afkQueueList.poll().kickPlayer("The server is full!\nWe've had to disconnect your alt to make place for real players");
			 Bukkit.broadcastMessage(Main.PREFIX + "An inactive player has been removed from the playing field to make space! Feel safe in your active minds!");
		 }
	 }
	 
	 public void addAfkPlayer(Player p) {
		 ServerSlotManager.INSTANCE.afkQueueList.add(p);
		 Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + p.getUniqueId().toString() + " permission set essentials.afk.kickexempt true");
	 }
	 
	 public void removeAfkPlayer(Player p) {
		 ServerSlotManager.INSTANCE.afkQueueList.remove(p);
		 Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + p.getUniqueId().toString() + " permission set essentials.afk.kickexempt false");
	 }	 

    public ServerSlotManager(){}

	public static final ServerSlotManager INSTANCE = new ServerSlotManager();
}