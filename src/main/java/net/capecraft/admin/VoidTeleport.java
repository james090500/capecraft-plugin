package net.capecraft.admin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class VoidTeleport implements Listener {

    @EventHandler
    public void onPlayerInVoid(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        if(p.getLocation().getY() < -10) {
            p.teleport(Bukkit.getWorld("spawn").getSpawnLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        }
    }

}
