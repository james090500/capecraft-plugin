package net.capecraft.admin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class VoidTeleport implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInVoid(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player) {
            Player p = (Player) event.getEntity();
            if(event.getCause().equals(EntityDamageEvent.DamageCause.VOID)) {
                p.teleport(Bukkit.getWorld("spawn").getSpawnLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
                event.setCancelled(true);
            }
        }
    }

}
