package net.capecraft.commands.rantp;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import net.capecraft.commands.utils.AntiCheese;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.capecraft.Main;

public class RandomTP implements CommandExecutor {
	
	//Max X and Z Blocks
	private static final int MAX_X = 100000;	
	private static final int MAX_Z = 100000;
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if(sender instanceof Player) {
			if(commandLabel.equalsIgnoreCase("wild") || commandLabel.equalsIgnoreCase("rtp")) {			
				this.teleportPlayer((Player) sender);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Teleport's the player to a random coordinates
	 * @param player
	 */
	private void teleportPlayer(Player player) {
		//Check if the player is past the cooldown time
		Long timePast = System.currentTimeMillis() - CoolDownManager.INSTANCE.getCooldown(player.getUniqueId());
		int timeLeft = (int) (CoolDownManager.DEFAULT_COOLDOWN - TimeUnit.MILLISECONDS.toSeconds(timePast));

		//If player is past cooldown and not in combat
		if(TimeUnit.MILLISECONDS.toSeconds(timePast) >= CoolDownManager.DEFAULT_COOLDOWN) {
			if(AntiCheese.INSTANCE.isInCombat(player)){
				//Will generate random number between -MAX and MAX
				Random rand = new Random();
				int RAND_X = rand.nextInt(MAX_X + 1 + MAX_X) - MAX_X;
				int RAND_Z = rand.nextInt(MAX_Z + 1 + MAX_Z) - MAX_Z;

				//Add slow falling to the player
				player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, (20 * 20), 0), true);
				
				//Teleport player and send them a message
				player.teleport(new Location(Bukkit.getWorld("survival"), RAND_X, 150, RAND_Z));
				player.sendMessage(Main.PREFIX + "You have been teleported to the wild!");
				
				//Will give player 
				CoolDownManager.INSTANCE.setCooldown(player.getUniqueId(), System.currentTimeMillis());
			}
		} else {
			player.sendMessage(Main.PREFIX + "You'll need to wait " + timeLeft + " seconds before you can do that again.");
		}

	}

}
