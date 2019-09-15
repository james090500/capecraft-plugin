package net.capecraft.events.afkcheck;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import net.capecraft.events.ServerSlotManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class AfkCheck implements Listener {
	
	/**
	 * Checks move event and tells player if they're afk
	 * @param event
	 */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMoveEvent(PlayerMoveEvent event){    	
        Player player = event.getPlayer();        
        //Send AFK players constant notifications when moving
        if(ServerSlotManager.INSTANCE.afkQueueList.contains(player)){
            BaseComponent[] msg = new ComponentBuilder("You're AFK! ").color(ChatColor.RED).bold( true ).append("You don't gain playtime while AFK ").color(ChatColor.GREEN).bold( true ).append(":'( ").bold( true ).color(ChatColor.AQUA).append("Use ./afk to play normally!").color(ChatColor.GREEN).bold( true ).create();
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, msg);
        }
    }


}
