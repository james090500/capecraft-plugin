package net.capecraft.commands.rantp;

import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class CooldownManager {

    private Logger log = Bukkit.getLogger();;
	
	//Will contain the uuid of the users and when they executed the command
	private HashMap<UUID, Long> cooldowns = new HashMap<>();
	private HashMap<UUID, Long> damageEvent = new HashMap<>();
	//60 second cooldown between commands
    public static final int DEFAULT_COOLDOWN = 10;

    //Will add the cooldown to a player or will remove the player from the cooldown.
    public void setCooldown(UUID player, long time){
        if(time == 0) {
            cooldowns.remove(player);
        } else {
            cooldowns.put(player, time);
        }
    }

    //Will get the time in seconds left of the player
    public long getCooldown(UUID player){
        return (cooldowns.get(player) == null ? Long.valueOf(0) : cooldowns.get(player));
    }

    //Will add the damageEvent to a player or will remove the player from the damageEvent list.
    public void setDamageEvent(UUID player, long time){
        if(time == 0) {
            damageEvent.remove(player);
        } else {
            damageEvent.put(player, time);
        }
    }

    public boolean getDamageEvent(UUID player){
        if(damageEvent.containsKey(player)){
            long time = damageEvent.get(player);
            long timePast = System.currentTimeMillis() - time;
            return TimeUnit.MILLISECONDS.toSeconds(timePast) > DEFAULT_COOLDOWN;
        }
        return true;
    }


    private CooldownManager(){}

    public static final CooldownManager INSTANCE = new CooldownManager();
}
