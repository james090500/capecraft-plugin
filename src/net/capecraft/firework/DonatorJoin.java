package net.capecraft.firework;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

public class DonatorJoin implements Listener {
	
	Plugin plugin;
	
	public DonatorJoin(Plugin p) {
		this.plugin = p;
	}

	@EventHandler
	public void onDonatorJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		if(p.hasPermission("capecraft.joinfirework")); {
			new RandomFirework(p, true);
			
			Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
				@Override
				public void run() {
					new RandomFirework(p, true);
				}				
			}, 20);
			
			Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
				@Override
				public void run() {
					new RandomFirework(p, true);
				}				
			}, 40);
		}
	}
	
}
