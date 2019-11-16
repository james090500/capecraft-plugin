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
	
	private String[] blockedMessages = {"/t", "/w", "/msg", "/tell", "/whisper", "/m"};
	private String[] blockedCommands = {"/pay"};
	private String[] blockedMovement = {"/tpa", "/tpask"};

    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
    	//Split the command
        String[] args = event.getMessage().split(" ");
        
        //Make sure the args are not 0
        if(args.length != 0) {        	
        	//Check if command is a blocked command
            String commandLabel = args[0];
            if(ArrayUtils.contains(blockedMessages, commandLabel.toLowerCase())|ArrayUtils.contains(blockedCommands, commandLabel.toLowerCase())|ArrayUtils.contains(blockedMovement, commandLabel.toLowerCase())) {
            	//See if the msg is to a player
            	if(args.length > 1) {
            		Player player = Bukkit.getPlayer(args[1]);
	                if(player != null) {
							if(ArrayUtils.contains(blockedMessages, commandLabel.toLowerCase())){
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
							} else if (ArrayUtils.contains(blockedCommands, commandLabel.toLowerCase())){
								if (player.hasPermission("group.alt")) {
									event.setCancelled(true);
									String msg = (ChatColor.RED + "" +  ChatColor.BOLD + "[CC] " +ChatColor.RESET + "" + ChatColor.RED + "If you pay an alt the money will be lost! Don't do that!");
									event.getPlayer().sendMessage(msg);
								} else if(ServerSlotManager.INSTANCE.afkQueueList.contains(player)){
									event.setCancelled(true);
									String msg = (ChatColor.RED + "" +  ChatColor.BOLD + "[CC] " +ChatColor.RESET + "" + ChatColor.RED + "This player is AFK and unlikely to notice your payment! ");
									event.getPlayer().sendMessage(msg);
									String msg2 = (ChatColor.RED + "" +  ChatColor.BOLD + "[CC] " +ChatColor.RESET + "" + ChatColor.RED + "Your payment has been canceled, try again later!");
									event.getPlayer().sendMessage(msg2);
								}
							} else if (ArrayUtils.contains(blockedMovement, commandLabel.toLowerCase())){
								if (player.hasPermission("group.alt")) {
									event.setCancelled(true);
									String msg = (ChatColor.RED + "" +  ChatColor.BOLD + "[CC] " +ChatColor.RESET + "" + ChatColor.RED + "You can not tp to alts! You will have to teleport the alt to your location.");
									event.getPlayer().sendMessage(msg);
								} else if(ServerSlotManager.INSTANCE.afkQueueList.contains(player)){
									event.setCancelled(true);
									String msg = (ChatColor.RED + "" +  ChatColor.BOLD + "[CC] " +ChatColor.RESET + "" + ChatColor.RED + "You can not tp to afk players!");
									event.getPlayer().sendMessage(msg);
									String msg2 = (ChatColor.RED + "" +  ChatColor.BOLD + "[CC] " +ChatColor.RESET + "" + ChatColor.RED + "Your request to tp has been canceled, wait until they are no longer afk.");
									event.getPlayer().sendMessage(msg2);
								}
							}

						}
					}
            	}
            }
        }
    }
