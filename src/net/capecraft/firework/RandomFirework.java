package net.capecraft.firework;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

public class RandomFirework  {
	
	public RandomFirework(Player player, boolean randomLoc) {
		random(player.getLocation(), randomLoc);
	}

	private static void random(Location loc, boolean randomLoc) {
		Random random = new Random();
		Firework firework;
		
		if(randomLoc) {
			loc.setX(loc.getX() + (random.nextInt(10) - 5));
			loc.setZ(loc.getZ() + (random.nextInt(10) - 5));
			firework = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
		} else {
			firework = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
		}
		FireworkMeta fireworkMeta = firework.getFireworkMeta();				
		FireworkEffect effect = FireworkEffect.builder().flicker(random.nextBoolean()).withColor(getColor(random.nextInt(17) + 1)).withFade(getColor(random.nextInt(17) + 1)).with(Type.values()[random.nextInt(Type.values().length)]).trail(random.nextBoolean()).build();		
		fireworkMeta.addEffect(effect);
		fireworkMeta.setPower(1);
		firework.setFireworkMeta(fireworkMeta);
	}

	private static Color getColor(final int i) {
		switch (i) {
		case 1:
			return Color.AQUA;
		case 2:
			return Color.BLACK;
		case 3:
			return Color.BLUE;
		case 4:
			return Color.FUCHSIA;
		case 5:
			return Color.GRAY;
		case 6:
			return Color.GREEN;
		case 7:
			return Color.LIME;
		case 8:
			return Color.MAROON;
		case 9:
			return Color.NAVY;
		case 10:
			return Color.OLIVE;
		case 11:
			return Color.ORANGE;
		case 12:
			return Color.PURPLE;
		case 13:
			return Color.RED;
		case 14:
			return Color.SILVER;
		case 15:
			return Color.TEAL;
		case 16:
			return Color.WHITE;
		case 17:
			return Color.YELLOW;
		}
		return null;
	}
	
}
