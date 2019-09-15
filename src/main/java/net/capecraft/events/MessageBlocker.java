package net.capecraft.events;

import net.md_5.bungee.api.ChatColor;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class MessageBlocker implements Listener {
	
	private String[] blockedCommands = {"/r", "/t", "/w", "/msg", "/tell", "/whisper"};
	
    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
    	//Split the command
        String[] args = event.getMessage().split(" ");
        
        //Make sure the args are not 0
        if(args.length != 0) {        	
        	//Check if command is a blocked command
            String commandLabel = args[0];
            if(ArrayUtils.contains(blockedCommands, commandLabel.toLowerCase())) {
            	//See if the msg is to a player
            	if(args.length > 1) {
            		String user = args[1];
	                if(Bukkit.getPlayer(user) != null) {
	                	Player player = Bukkit.getPlayer(user);
	                    if (player.hasPermission("group.alt")) {
	                        event.setCancelled(true);
	                        String msg = (ChatColor.RED + "" +  ChatColor.BOLD + "[CC] " +ChatColor.RESET + "" + ChatColor.RED + "You cant message an alt! They are muted!");
	                        event.getPlayer().sendMessage(msg);
	                    } else if(ServerSlotManager.INSTANCE.afkQueueList.contains(player)){
	                        event.setCancelled(true);
	                        String msg = (ChatColor.RED + "" +  ChatColor.BOLD + "[CC] " +ChatColor.RESET + "" + ChatColor.RED + "This player is AFK and unlikely to respond! ");
	                        event.getPlayer().sendMessage(msg);
	                        String msg2 = (ChatColor.RED + "" +  ChatColor.BOLD + "[CC] " +ChatColor.RESET + "" + ChatColor.RED + "Your message has been canceled, try again later!");
	                        event.getPlayer().sendMessage(msg2);
	                    }
	                }
            	}
            }
        }
    }
}
