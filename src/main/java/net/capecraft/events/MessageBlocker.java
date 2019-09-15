package net.capecraft.events;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class MessageBlocker implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void PlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
        String[] args = event.getMessage().split(" ");
        if(args.length != 0){
            String commandLabel = args[0];
            if (commandLabel.equalsIgnoreCase("/r")|
                    commandLabel.equalsIgnoreCase("/t")|
                    commandLabel.equalsIgnoreCase("/w")|
                    commandLabel.equalsIgnoreCase("/msg")|
                    commandLabel.equalsIgnoreCase("/tell")|
                    commandLabel.equalsIgnoreCase("/whisper")) {
                if (args.length > 1) {
                    String user = args[1];
                    if (Bukkit.getPlayer(user) != null) {
                        Player p = Bukkit.getPlayer(user);
                        assert p != null;
                        if (p.hasPermission("group.alt")) {
                            event.setCancelled(true);
                            String msg = (ChatColor.RED + "" +  ChatColor.BOLD + "[CC] " +ChatColor.RESET + "" + ChatColor.RED + "You cant message an alt! They are muted!");
                            event.getPlayer().sendMessage(msg);
                        }else if(ServerSlotManager.INSTANCE.afkQueueList.contains(p)){
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
