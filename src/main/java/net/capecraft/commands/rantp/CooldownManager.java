package net.capecraft.commands.rantp;

import java.util.HashMap;
import java.util.UUID;

public class CooldownManager {
	
	//Will contain the uuid of the users and when they executed the command
	private HashMap<UUID, Long> cooldowns = new HashMap<>();
	//60 second cooldown between commands
    public static final int DEFAULT_COOLDOWN = 300;

    //Will add the cooldown to a player or will remove the player from the cooldown.
    public void setCooldown(UUID player, long time){
    	cooldowns.put(player, time);
    }

    //Will get the time in seconds left of the player
    public long getCooldown(UUID player){
    	return (cooldowns.get(player) == null ? Long.valueOf(0) : cooldowns.get(player));
    }

    private CooldownManager(){}

    public static final CooldownManager INSTANCE = new CooldownManager();
}
