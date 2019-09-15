package net.capecraft.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.capecraft.events.comSpy.ComSpy;
import net.md_5.bungee.api.ChatColor;

public class ComSpyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player){
            if (s.equalsIgnoreCase("comspy")) {
                Player p = (Player) commandSender;
                //Checks player has permission to spy on commands
                if(commandSender.hasPermission("capecraft.comSpy")){
                	//If player is already listening then remove them, else add them to the listener!
                    if(ComSpy.INSTANCE.ComListener.contains(p)){
                    	//remove from listening
                        ComSpy.INSTANCE.removeComListener(p);                        
                        String msg = (ChatColor.RED + "" +  ChatColor.BOLD + "[CC] " +ChatColor.RESET + "" + ChatColor.RED + "CommandSpy disabled");
                        p.sendMessage(msg);
                        return true;
                    }else{
                    	//add to listener
                        ComSpy.INSTANCE.addComListener(p);                        
                        String msg = (ChatColor.RED + "" +  ChatColor.BOLD + "[CC] " +ChatColor.RESET + "" + ChatColor.GREEN + "CommandSpy enabled");
                        p.sendMessage(msg);
                        return true;
                    }
                } else {
                	//Permission denied!
                    String msg = (ChatColor.RED + "" +  ChatColor.BOLD + "[CC] " +ChatColor.RESET + "" + ChatColor.GREEN + "You have not the power to listen to this. LEAVE!");
                    p.sendMessage(msg);
                }
            }
        }
        return false;
    }
}
