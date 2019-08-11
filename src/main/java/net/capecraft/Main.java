package net.capecraft;

import org.bukkit.plugin.java.JavaPlugin;

import net.capecraft.admin.ServerSlotManager;
import net.capecraft.admin.VoidTeleport;
import net.capecraft.commands.PluginList;
import net.capecraft.member.MemberCommands;
import net.capecraft.member.MemberConfig;
import net.capecraft.protect.ArmorStandProtect;
import net.capecraft.protect.ItemFrameProtect;

public class Main extends JavaPlugin {

	public static final String PREFIX = "§c§lCapeCraft §9»§r ";

	@Override
	public void onEnable() {

		//Wild
		getCommand("wild").setExecutor(new PluginList(this));
		getCommand("rtp").setExecutor(new PluginList(this));

		//Playtime
		getCommand("playtime").setExecutor(new MemberCommands(this));
		getCommand("playtimetop").setExecutor(new MemberCommands(this));
		
		//Cape
		getCommand("cape").setExecutor(new MemberCommands(this));
		
		
		//Events
		getServer().getPluginManager().registerEvents(new ServerSlotManager(), this);
		getServer().getPluginManager().registerEvents(new VoidTeleport(), this);
		getServer().getPluginManager().registerEvents(new MemberConfig(this), this);
		getServer().getPluginManager().registerEvents(new ArmorStandProtect(this), this);
		getServer().getPluginManager().registerEvents(new ItemFrameProtect(this), this);
	}
}