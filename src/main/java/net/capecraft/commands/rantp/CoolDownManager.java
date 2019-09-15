package net.capecraft.commands.rantp;

import java.util.HashMap;
import java.util.UUID;

public class CoolDownManager {

	//The Singleton
	public static final CoolDownManager INSTANCE = new CoolDownManager();
    //Will contain the uuid of the users and when they executed the command
	private HashMap<UUID, Long> cooldowns = new HashMap<>();
	//60 second cooldown between commands
    public static final int DEFAULT_COOLDOWN = 300;

    /**
     * Constructor for the singleton
     */
    private CoolDownManager(){}
    
    /**
     * Add player to the cooldown list
     * @param player
     * @param time
     */
    public void setCooldown(UUID player, long time){
        cooldowns.put(player, time);
    }

    /**
     * Will return how many seconds left in the cooldown
     * @param player
     * @return
     */
    public long getCooldown(UUID player){
        return (cooldowns.get(player) == null ? Long.valueOf(0) : cooldowns.get(player));
    }    
}
