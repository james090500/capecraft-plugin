package net.capecraft.commands.utils;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

import net.capecraft.Main;

public class AntiCheese {

	//Instance and hashmap creation
	public static final AntiCheese INSTANCE = new AntiCheese();
    private HashMap<UUID, Long> damageEvent = new HashMap<>();

    /**
     * Constructor for singleton
     */
    private AntiCheese(){}
    
    /**
     * Sets the a player to be damaged
     * @param player
     * @param time
     */
    public void setDamageEvent(Player player, long combatInSeconds) {
    	//Calculates time when player will no longer be in combat and insert its to the hashmap
    	long combatTime = System.currentTimeMillis() + (combatInSeconds * 1000);
        damageEvent.put(player.getUniqueId(), combatTime);
    }

    /**
     * Check if player is in combat
     * @param player
     * @return
     */
	public boolean isInCombat(Player player) {
		//Get player uuid
		UUID uuid = player.getUniqueId();
		
		//Check if uuid is in damage hashmap
		if(damageEvent.containsKey(uuid)) {			
			//If it is, check the value is less than current time
			if(damageEvent.get(uuid) < System.currentTimeMillis()) {
				//Remove from hashmap as player not in combat
				damageEvent.remove(uuid);
				return false;
			}
			//Player is in combat
			player.sendMessage(Main.PREFIX + "You have taken damage, you need to be in a safe place to use this command!");
			return true;
		} else {
			return false;
		}
	}
}
