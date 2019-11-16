package net.capecraft.events;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class MessageBlocker implements Listener {
	
	private ComponentBuilder prefix = new ComponentBuilder("[CC] ").bold(true).color(ChatColor.RED);

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
	                	BaseComponent[] msg = null;
	                	BaseComponent[] blockMsg;
                	
						if(ArrayUtils.contains(blockedMessages, commandLabel.toLowerCase())) {							
							if (player.hasPermission("group.alt")) {
								msg = prefix.append("You cant message an alt! They are muted!").bold(false).create();
							} else if(ServerSlotManager.INSTANCE.afkQueueList.contains(player)){
								msg = prefix.append("This player is AFK and unlikely to respond!").bold(false).create();									
							}
							blockMsg = prefix.append("Your message has been canceled, try again later!").bold(false).create();
						} else if (ArrayUtils.contains(blockedCommands, commandLabel.toLowerCase())){							
							if (player.hasPermission("group.alt")) {
								msg = prefix.append("If you pay an alt the money will be lost! Don't do that!").bold(false).create();
							} else if(ServerSlotManager.INSTANCE.afkQueueList.contains(player)){
								msg = prefix.append("This player is AFK and unlikely to notice your payment!").bold(false).create();									
							}
							blockMsg = prefix.append("Your payment has been canceled, try again later!").bold(false).create();
						} else if (ArrayUtils.contains(blockedMovement, commandLabel.toLowerCase())){							
							if (player.hasPermission("group.alt")) {									
								msg = prefix.append("You can not tp to alts! You will have to teleport the alt to your location.").bold(false).create();
							} else if(ServerSlotManager.INSTANCE.afkQueueList.contains(player)){
								msg = prefix.append("You can not tp to afk players!").bold(false).create();									
							}
							blockMsg = prefix.append("Your request to tp has been canceled, wait until they are no longer afk.").bold(false).create();
						} else {
							return;
						}
						
						event.setCancelled(true);
						event.getPlayer().spigot().sendMessage(msg);
						event.getPlayer().spigot().sendMessage(blockMsg);						
	                }
            	}
            }
        }
    }
}