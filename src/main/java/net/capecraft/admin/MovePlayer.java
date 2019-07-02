package net.capecraft.admin;

import java.text.DecimalFormat;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.james090500.APIManager.UserInfo;

import net.capecraft.Main;

public class MovePlayer implements CommandExecutor {

	Plugin instance;

	public MovePlayer(Plugin instance) {
		this.instance = instance;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String commandLabel, String[] args) {
		if (commandSender instanceof Player) {
			Player sender = (Player) commandSender;
			if (commandLabel.equalsIgnoreCase("moveplayer")) {
				if (args.length == 0) {
					sender.sendMessage(Main.PREFIX + "Syntax for moving a player");
					sender.sendMessage("§c/moveplayer <player> §e| Information about the player");
					sender.sendMessage("§c/moveplayer <player> here §e| Moves the player to you");
				} else if (args.length == 1) {
					movePlayerInfo(sender, args[0]);
					return true;
				} else if (args.length == 2 && args[1].equalsIgnoreCase("here")) {
					movePlayerToOp(sender, args[0]);
					return true;
				}
			}
		}
		return false;
	}

	private void movePlayerInfo(Player sender, String target) {
		String parsedUUID = UserInfo.getParsedUUID(target);
		if (parsedUUID != null) {
			UUID targetUUID = UUID.fromString(parsedUUID);
			OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(targetUUID);
			if (offlinePlayer.hasPlayedBefore()) {
				Location opLocation = offlinePlayer.getPlayer().getLocation();
				
				DecimalFormat df = new DecimalFormat();
				df.setMaximumFractionDigits(2);
				
				String xCord = df.format(opLocation.getX());
				String yCord = df.format(opLocation.getY());
				String zCord = df.format(opLocation.getZ());
				
				sender.sendMessage(String.format(Main.PREFIX + target + " is currently at %s %s %s", xCord, yCord, zCord));
			} else {				
				sender.sendMessage(Main.PREFIX + "That user doesn't exist");
			}
		} else {			
			sender.sendMessage(Main.PREFIX + "That user doesn't exist");
		}
	}

	private void movePlayerToOp(Player sender, String target) {
		String parsedUUID = UserInfo.getParsedUUID(target);
		if (parsedUUID != null) {
			UUID targetUUID = UUID.fromString(UserInfo.getParsedUUID(target));
			OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(targetUUID);
			if (offlinePlayer.hasPlayedBefore()) {
				offlinePlayer.getPlayer().getLocation().setX(sender.getLocation().getX());
				offlinePlayer.getPlayer().getLocation().setY(sender.getLocation().getY());
				offlinePlayer.getPlayer().getLocation().setZ(sender.getLocation().getZ());
				sender.sendMessage(String.format(Main.PREFIX + target + " user has been teleported to you"));
			} else {
				sender.sendMessage(Main.PREFIX + "That user doesn't exist");
			}
		} else {
			sender.sendMessage(Main.PREFIX + "That user doesn't exist");
		}
	}
}
