package net.capecraft.rantp;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.capecraft.Main;
import net.capecraft.commands.CooldownManager;

public class RandomTP {
	
	private static final int MAX_X = 10000;	
	private static final int MAX_Z = 10000;
	
	public void teleportPlayer(Player player) {
		//Check if the player is past the cooldown time
		Long timePast = System.currentTimeMillis() - CooldownManager.INSTANCE.getCooldown(player.getUniqueId());
		int timeLeft = (int) (60 - TimeUnit.MILLISECONDS.toSeconds(timePast));	

		//If player is past cooldown
		if(TimeUnit.MILLISECONDS.toSeconds(timePast) >= CooldownManager.DEFAULT_COOLDOWN) {
			Random rand = new Random();
			int RAND_X = rand.nextInt(MAX_X + 1 + MAX_X) - MAX_X;
			int RAND_Z = rand.nextInt(MAX_Z + 1 + MAX_Z) - MAX_Z;			
			
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, (20 * 20), 0), true);
			
			player.teleport(new Location(Bukkit.getWorld("survival"), player.getLocation().getX() + RAND_X, 150, player.getLocation().getZ() + RAND_Z));
			player.sendMessage(Main.PREFIX + "You have been teleported to the wild!");
			CooldownManager.INSTANCE.setCooldown(player.getUniqueId(), System.currentTimeMillis());
		} else {
			player.sendMessage(Main.PREFIX + "You'll need to wait " + timeLeft + " seconds before you can do that again.");
		}
	}
}
