package net.capecraft.events.comSpy;

import java.util.LinkedList;
import java.util.Queue;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import net.md_5.bungee.api.ChatColor;

public class ComSpy implements Listener {

	public static final ComSpy INSTANCE = new ComSpy();
    public Queue<Player> ComListener = new LinkedList<>();

    /**
     * Constructor for singleton
     */
    public ComSpy(){}
    
    /**
     * Get all commands and send to comspy players
     * @param event
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
        Player sender = event.getPlayer();
        String command = event.getMessage();
        String name = sender.getName();

        //Send players command (except their own)
        String msg = (ChatColor.RED + "" +  ChatColor.BOLD + "[CC] " +ChatColor.RESET + "" + ChatColor.YELLOW + name + ": " + command);
        for (Player p : ComSpy.INSTANCE.ComListener){
        	if(sender != p) {
        		p.sendMessage(msg);
        	}
        }
    }

    /**
     * Add Player to comspy listener
     * @param p
     */
    public void addComListener(Player p) {
        //Adds player to the queue's singleton instance - mov51
        ComSpy.INSTANCE.ComListener.add(p);
    }

    /**
     * Remove player from company listender
     * @param p
     */
    public void removeComListener(Player p) {
        //Removes player from the queue's singleton instance - mov51
        ComSpy.INSTANCE.ComListener.remove(p);
    }
}

