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

    public Queue<Player> ComListener = new LinkedList<>();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
        Player sender = event.getPlayer();
        String command = event.getMessage();
        String name = sender.getName();

        String msg = (ChatColor.RED + "" +  ChatColor.BOLD + "[CC] " +ChatColor.RESET + "" + ChatColor.YELLOW + name + ": " + command);
        for (Player p : ComSpy.INSTANCE.ComListener){
        	if(sender != p) {
        		p.sendMessage(msg);
        	}
        }
    }

    public void addComListener(Player p) {
        //Adds player to the queue's singleton instance - mov51
        ComSpy.INSTANCE.ComListener.add(p);
    }

    public void removeComListener(Player p) {
        //Removes player from the queue's singleton instance - mov51
        ComSpy.INSTANCE.ComListener.remove(p);
    }

    //Access singleton instance - mov51
    public ComSpy(){}
    //Create singleton instance - mov51
    public static final ComSpy INSTANCE = new ComSpy();
}

