package net.capecraft.commands.utils;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.entity.Player;

public class AntiCheese {

    private HashMap<UUID, Long> damageEvent = new HashMap<>();

    public void setDamageEvent(Player player, long time){
        damageEvent.put(player.getUniqueId(), time);
    }

    //Will add the damageEvent to a player or will remove the player from the damageEvent list.
    public boolean getDamageEvent(UUID player, long timeCheckInSec){
        if(damageEvent.containsKey(player)){
            long time = damageEvent.get(player);
            long timePast = System.currentTimeMillis() - time;
            return TimeUnit.MILLISECONDS.toSeconds(timePast) > timeCheckInSec;
        }
        return true;
    }

    private AntiCheese(){}

    public static final AntiCheese INSTANCE = new AntiCheese();
}
