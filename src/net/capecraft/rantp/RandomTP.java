package net.capecraft.rantp;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;

import net.capecraft.Main;
import net.capecraft.commands.CooldownManager;

public class RandomTP {
	
	private static final int MAX_X = 10000;	
	private static final int MAX_Z = 10000;
	
	public void teleportPlayer(Player player) {
		if(player.getWorld().getEnvironment() == Environment.NORMAL) {
			Long timePast = System.currentTimeMillis() - CooldownManager.INSTANCE.getCooldown(player.getUniqueId());
			int timeLeft = (int) (60 - TimeUnit.MILLISECONDS.toSeconds(timePast));	
			
			if(TimeUnit.MILLISECONDS.toSeconds(timePast) >= CooldownManager.DEFAULT_COOLDOWN) {
				Random rand = new Random();
				int RAND_X = rand.nextInt(MAX_X + 1 + MAX_X) - MAX_X;
				int RAND_Z = rand.nextInt(MAX_Z + 1 + MAX_Z) - MAX_Z;
				int SAFE_Y = player.getWorld().getHighestBlockYAt(RAND_X, RAND_Z);
				player.teleport(new Location(player.getWorld(), RAND_X, SAFE_Y, RAND_Z));
				player.sendMessage(Main.PREFIX + "You have been teleported to the wild!");
				CooldownManager.INSTANCE.setCooldown(player.getUniqueId(), System.currentTimeMillis());
			} else {
				player.sendMessage(Main.PREFIX + "You'll need to wait " + timeLeft + " seconds before you can do that again.");
			}
		} else {
			player.sendMessage(Main.PREFIX + "You can only do /wild in the overworld");
		}
	}
	
}
