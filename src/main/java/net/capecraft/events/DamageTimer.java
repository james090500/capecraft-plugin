package net.capecraft.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import net.capecraft.commands.utils.AntiCheese;

public class DamageTimer implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent event){
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            AntiCheese.INSTANCE.setDamageEvent(player, System.currentTimeMillis());
        }
    }
}
