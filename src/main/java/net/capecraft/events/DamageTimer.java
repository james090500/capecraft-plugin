package net.capecraft.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import net.capecraft.commands.utils.AntiCheese;

public class DamageTimer implements Listener {

	/**
	 * On Damage puts players in combat
	 */
    @EventHandler
    public void onDamage(EntityDamageEvent event){
        if (event.getEntity() instanceof Player) {            
        	//Puts player in combat mode for 5 seconds
            AntiCheese.INSTANCE.setDamageEvent((Player) event.getEntity());
        }
    }
}
