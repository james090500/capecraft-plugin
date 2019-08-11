package net.capecraft.protect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import net.capecraft.Main;
import net.capecraft.protect.utils.SQLUtils;
import net.capecraft.protect.utils.Utils;

public class ItemFrameProtect implements Listener {

	public static Logger logger = null;
	public static PluginDescriptionFile pdfFile = null;
	public static String dbName = null;

	private static UUID itemFrameUuid = null;
	private static long itemFrameUuidTime = 0;
	private static Player itemFramePlayer = null;
	private static long itemFramePlayerTime = 0;

	Plugin instance;

	public ItemFrameProtect(Plugin instance) {
		this.instance = instance;

		ItemFrameProtect.pdfFile = instance.getDescription();
		ItemFrameProtect.logger = Logger.getLogger("Minecraft");
		ItemFrameProtect.dbName = "plugins/CapeCraft/ProtectedEntities.db";
		SQLUtils.sqlInit(dbName);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void entitySpawnEventHandler(EntitySpawnEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof ItemFrame) {
			ItemFrame itemFrame = (ItemFrame) entity;
			if (itemFrame != null) {
				itemFrameUuid = itemFrame.getUniqueId();
				itemFrameUuidTime = System.currentTimeMillis();
				czechItemFrame();
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void playerInteractEventHandler(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (event.getItem() != null) {
			ItemStack item = event.getItem();
			Material material = item.getType();
			if (material.equals(Material.ITEM_FRAME)) {
				itemFramePlayer = player;
				itemFramePlayerTime = System.currentTimeMillis();
				czechItemFrame();
			}
		}
	}

	private void czechItemFrame() {
		if ((itemFrameUuidTime > 0) && (itemFramePlayerTime > 0) && (itemFramePlayer != null) && (itemFrameUuid != null)) {
			long diff = Math.abs(itemFrameUuidTime - itemFramePlayerTime);
			if (diff < 10) {
				Connection connection = SQLUtils.sqlOpen(dbName);
				try {
					PreparedStatement statement = connection.prepareStatement("insert into ProtectedEntities values(?, ?, ?); ");
					statement.setString(1, Utils.hexEncode(itemFrameUuid.toString()));
					statement.setString(2, Utils.hexEncode(itemFramePlayer.getUniqueId().toString()));
					statement.setString(3, Utils.hexEncode(itemFramePlayer.getName()));
					SQLUtils.sqlUpdate(connection, statement);
				} catch (Exception oops) {
					logger.severe("[" + pdfFile.getName() + "] Exception: '" + oops.getMessage() + "'. ");
					oops.printStackTrace(System.err);
				} finally {
					SQLUtils.sqlClose(connection);
				}
				itemFrameUuid = null;
				itemFrameUuidTime = 0;
				itemFramePlayer = null;
				itemFramePlayerTime = 0;
			}
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void hangingBreakByEntityEventHandler(HangingBreakByEntityEvent event) {
		if (!event.isCancelled()) {
			Entity entity = event.getEntity();

			if (entity instanceof ItemFrame) {										
				Entity damager = event.getRemover();
				
				if(damager instanceof Player) {
					event.setCancelled(startRemoveProcess((Player) damager, (ItemFrame) entity));				
				}
				
				if(damager instanceof Projectile) {					
					if(((Projectile) damager).getShooter() instanceof Player) {
						event.setCancelled(startRemoveProcess(((Player) ((Projectile) damager).getShooter()), (ItemFrame) entity));
					}
				}				
			}			
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void playerItemFrameManipulateEventHandler(PlayerInteractEntityEvent event) {
		if (!event.isCancelled()) {
			if (event.getRightClicked() instanceof ItemFrame) {
				event.setCancelled(startRemoveProcess(event.getPlayer(), (ItemFrame) event.getRightClicked()));
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void entityDamageByEntityEventHandler(EntityDamageByEntityEvent event) {		
		if (!event.isCancelled()) {
			Entity entity = event.getEntity();
			if (entity instanceof ItemFrame) {										
				Entity damager = event.getDamager();
				
				if(damager instanceof Player) {
					event.setCancelled(startRemoveProcess((Player) damager, (ItemFrame) entity));				
				}
				
				if(damager instanceof Projectile) {					
					if(((Projectile) damager).getShooter() instanceof Player) {
						event.setCancelled(startRemoveProcess(((Player) ((Projectile) damager).getShooter()), (ItemFrame) entity));
					}
				}				
			}
		}
	} 

	private boolean startRemoveProcess(Player player, ItemFrame itemFrame) {
		UUID ownerId = null;
		String ownerName = null;
		Connection connection = SQLUtils.sqlOpen(dbName);
		try {
			PreparedStatement statement = connection.prepareStatement("select * from ProtectedEntities where entity=?; ");
			statement.setString(1, Utils.hexEncode(itemFrame.getUniqueId().toString()));
			ResultSet data = SQLUtils.sqlQuery(connection, statement);
			if (data != null) {
				if (data.next()) {
					ownerId = UUID.fromString(Utils.hexDecode(data.getString("player")));
					ownerName = Utils.hexDecode(data.getString("username"));
				}
			} else {
				return false;
			}
		} catch (Exception oops) {
			logger.severe("[" + pdfFile.getName() + "] Exception: '" + oops.getMessage() + "'. ");
			oops.printStackTrace(System.err);
		} finally {
			SQLUtils.sqlClose(connection);
		}
		
		if(ownerId == null) {
			return false;
		}

		if (!player.getUniqueId().equals(ownerId)) {
			if (player.hasPermission("capecraft.admin")) {
				if (player.getInventory().getItemInMainHand().getType() == Material.CARROT_ON_A_STICK) {
					player.sendMessage(Main.PREFIX + "That item frame is owned by " + ownerName);
					return true;
				} else {
					return false;
				}
			} else {
				player.sendMessage(Main.PREFIX + "That item frame belongs to someone else!");
				return true;
			}
		} else {
			return false;	
		}		
	}
}