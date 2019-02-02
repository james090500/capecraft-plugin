package net.capecraft.protect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import net.capecraft.Main;
import net.capecraft.protect.utils.SQLUtils;
import net.capecraft.protect.utils.Utils;

public class ArmorStandProtect implements Listener {
   
	public static Logger logger = null;
	public static PluginDescriptionFile pdfFile = null;
	public static String dbName = null;
	
	private static UUID armorStandUuid = null;
	private static long armorStandUuidTime = 0;
	private static Player armorStandPlayer = null;
	private static long armorStandPlayerTime = 0;     
   
	Plugin instance;
	
	public ArmorStandProtect(Plugin instance) {
		this.instance = instance;
		
		ArmorStandProtect.pdfFile = instance.getDescription();        
		ArmorStandProtect.logger = Logger.getLogger("Minecraft");  
		ArmorStandProtect.dbName = "plugins/CapeCraft/ArmorStands.db";
		SQLUtils.sqlInit(dbName);	      	
	}
      
	@EventHandler (priority = EventPriority.MONITOR)
	public void entitySpawnEventHandler (EntitySpawnEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof ArmorStand) {
			ArmorStand armorStand = (ArmorStand) entity;
			if (armorStand != null) {
				armorStandUuid = armorStand.getUniqueId();
				armorStandUuidTime = System.currentTimeMillis();   
				czechArmorStand();
			}            
		}
	}
   
	@EventHandler (priority = EventPriority.MONITOR)
	public void playerInteractEventHandler (PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (event.getItem() != null) {
			ItemStack item = event.getItem();
			Material material = item.getType();
			if (material.equals(Material.ARMOR_STAND)) {
				armorStandPlayer = player;
				armorStandPlayerTime = System.currentTimeMillis();
				czechArmorStand();
			}
		}
	}     
	
	private void czechArmorStand() {
		if ((armorStandUuidTime > 0) && (armorStandPlayerTime > 0) && (armorStandPlayer != null) && (armorStandUuid != null)) {
			long diff = Math.abs(armorStandUuidTime - armorStandPlayerTime);
			if (diff < 10) {
				Connection connection = SQLUtils.sqlOpen(dbName);
				try {   
					PreparedStatement statement = connection.prepareStatement("insert into armorstand values(?, ?); ");
					statement.setString(1, Utils.hexEncode(armorStandUuid.toString()));
					statement.setString(2, Utils.hexEncode(armorStandPlayer.getUniqueId().toString()));
					SQLUtils.sqlUpdate(connection, statement);   
				} catch (Exception oops) {
	                 	logger.severe("[" + pdfFile.getName() + "] Exception: '" + oops.getMessage() + "'. ");       
	                 	oops.printStackTrace(System.err);           
				} finally {
					SQLUtils.sqlClose(connection);
				}
				armorStandUuid = null;
				armorStandUuidTime = 0;
				armorStandPlayer = null;
				armorStandPlayerTime = 0;   
			}
		}
	}
       	
	@EventHandler (priority = EventPriority.LOW)
	public void entityDamageByEntityEventHandler (EntityDamageByEntityEvent event) {
		if (event.isCancelled() == false) {
			Entity damager = event.getDamager();
			if (damager instanceof Player) {
				Player player = (Player) damager;
				Entity entity = event.getEntity();
				UUID ownerId = null;           
				if (entity instanceof ArmorStand) {
					ArmorStand armorStand = (ArmorStand) entity;
					if (armorStand != null) {
						Connection connection = SQLUtils.sqlOpen(dbName);
						try {   
							PreparedStatement statement = connection.prepareStatement("select * from armorstand where stand=?; ");
							statement.setString(1, Utils.hexEncode(armorStand.getUniqueId().toString()));
							ResultSet data = SQLUtils.sqlQuery(connection, statement);
							if (data != null) {
								if (data.next()) {
									ownerId = UUID.fromString(Utils.hexDecode(data.getString("player")));
								}
							}
						} catch (Exception oops) {
							logger.severe("[" + pdfFile.getName() + "] Exception: '" + oops.getMessage() + "'. ");       
							oops.printStackTrace(System.err);           
						} finally {
							SQLUtils.sqlClose(connection);
						}
						if (player.getUniqueId().equals(ownerId) == false) {							
							if (player.hasPermission("capecraft.admin") || ownerId == null) {                               
								return;	                       
							} else {
								event.setCancelled(true);
								player.sendMessage(Main.PREFIX + "That armor stand belongs to someone else. ");
							}
						}
					}                   
				}
			}
		}
	}
	   
	@EventHandler (priority = EventPriority.LOW)
	public void playerArmorStandManipulateEventHandler (PlayerArmorStandManipulateEvent event) {
		if (event.isCancelled() == false) {
			Player player = event.getPlayer();
			UUID ownerId = null;
			ArmorStand armorStand = event.getRightClicked();
			Connection connection = SQLUtils.sqlOpen(dbName);
			try {   
				PreparedStatement statement = connection.prepareStatement("select * from armorstand where stand=?; ");
				statement.setString(1, Utils.hexEncode(armorStand.getUniqueId().toString()));
				ResultSet data = SQLUtils.sqlQuery(connection, statement);
				if (data != null) {
					if (data.next()) {
						ownerId = UUID.fromString(Utils.hexDecode(data.getString("player")));
					}
				}
			} catch (Exception oops) {
				logger.severe("[" + pdfFile.getName() + "] Exception: '" + oops.getMessage() + "'. ");       
				oops.printStackTrace(System.err);           
			} finally {
				SQLUtils.sqlClose(connection);
			}
			if (player.getUniqueId().equals(ownerId) == false) {
				if (player.hasPermission("capecraft.admin")) {
					return;
				} else {
					event.setCancelled(true);
					player.sendMessage(Main.PREFIX + "That armor stand belongs to someone else. ");
				}
			}
		}
	}   
}