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
		 int playerCount = Bukkit.getServer().getOnlinePlayers().size();
		 int maxPlayerCount = Bukkit.getServer().getMaxPlayers();
		 List<Player> altsOnline = new ArrayList<Player>();
		 if (playerCount == (maxPlayerCount - 1)) {	         
			 Bukkit.getOnlinePlayers().stream().filter(p -> p.hasPermission("group.alt")).forEach(p -> altsOnline.add(p));
			 Random rand = new Random();
			 Player randomAlt = altsOnline.get(rand.nextInt(altsOnline.size()));
			 randomAlt.kickPlayer("The server is full!\nWe've had to disconnect your alt to make place for real players");
        }
	 }
	
}
