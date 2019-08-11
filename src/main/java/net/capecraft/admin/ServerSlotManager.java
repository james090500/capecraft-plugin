package net.capecraft.admin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class ServerSlotManager implements Listener {

	 @EventHandler(priority = EventPriority.HIGHEST)
	 public void onPlayerLoginEvent(PlayerLoginEvent event) {
		 if (event.getResult() == PlayerLoginEvent.Result.KICK_FULL) {	         
            if (event.getPlayer().hasPermission("capecraft.playerlimitbypass")) { 
                event.allow();
            }
        }
	 }
	
}
