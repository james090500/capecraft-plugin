package net.capecraft.events;

import net.capecraft.commands.utils.AntiCheese;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.logging.Logger;

public class DamageTimer implements Listener {

    private Logger log = Bukkit.getLogger();

    @EventHandler
    public void onDamage(EntityDamageEvent event){
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            AntiCheese.INSTANCE.setDamageEvent(player, System.currentTimeMillis());
        }
    }
}
