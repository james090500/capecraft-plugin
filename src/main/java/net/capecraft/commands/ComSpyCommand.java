package net.capecraft.commands;

import net.capecraft.events.comSpy.ComSpy;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ComSpyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player){
            if (s.equalsIgnoreCase("comspy")) {
                Player p = (Player) commandSender;
                if(commandSender.hasPermission("comSpy")){
                    if(ComSpy.INSTANCE.ComListener.contains(p)){
                        ComSpy.INSTANCE.removeComListener(p);
                        String msg = (ChatColor.RED + "" +  ChatColor.BOLD + "[CC] " +ChatColor.RESET + "" + ChatColor.RED + "CommandSpy disabled");
                        p.sendMessage(msg);
                        return true;
                    }else{
                        ComSpy.INSTANCE.addComListener(p);
                        String msg = (ChatColor.RED + "" +  ChatColor.BOLD + "[CC] " +ChatColor.RESET + "" + ChatColor.GREEN + "CommandSpy enabled");
                        p.sendMessage(msg);
                        return true;
                    }
                }else{
                    String msg = (ChatColor.RED + "" +  ChatColor.BOLD + "[CC] " +ChatColor.RESET + "" + ChatColor.GREEN + "You have not the power to listen to this. LEAVE!");
                    p.sendMessage(msg);
                }
            }
        }
        return false;
    }
}
