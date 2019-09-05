package net.capecraft;

import net.capecraft.member.afkCheck.AfkCheck;
import net.capecraft.member.afkCheck.IsAfkPlaceholder;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.capecraft.admin.ServerSlotManager;
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
		getCommand("afk").setExecutor(new MemberCommands(this));

		//Cape
		getCommand("cape").setExecutor(new MemberCommands(this));
		
		//KeepInv Toggle
		getCommand("keepinv").setExecutor(new PluginList(this));
		
		//Events
		getServer().getPluginManager().registerEvents(new ServerSlotManager(), this);
		getServer().getPluginManager().registerEvents(new AfkCheck(this), this);
		getServer().getPluginManager().registerEvents(new MemberConfig(this), this);
		getServer().getPluginManager().registerEvents(new ArmorStandProtect(this), this);
		getServer().getPluginManager().registerEvents(new ItemFrameProtect(this), this);

		//PlaceHolderAPI
		if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
			new IsAfkPlaceholder(this).register();
		} else {
			getServer().getLogger().log(Level.SEVERE, "PlaceHolderAPI Not found. Placeholders will not working");
		}
	}
}