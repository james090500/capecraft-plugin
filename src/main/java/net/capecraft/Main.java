package net.capecraft;

import org.bukkit.plugin.java.JavaPlugin;

import net.capecraft.commands.PluginList;
import net.capecraft.joinleave.MessageListener;
import net.capecraft.member.MemberCommands;
import net.capecraft.member.MemberConfig;
import net.capecraft.protect.ArmorStandProtect;

public class Main extends JavaPlugin {

	public static final String PREFIX = "\ufffdc\ufffdlCapeCraft \ufffd8\ufffdl\ufffd\ufffdr ";

	@Override
	public void onEnable() {

		//Wild
		getCommand("wild").setExecutor(new PluginList(this));
		getCommand("rtp").setExecutor(new PluginList(this));
		
		//Playtime
		getCommand("playtime").setExecutor(new MemberCommands(this));
		getCommand("playtimetop").setExecutor(new MemberCommands(this));

		//Events		
		getServer().getPluginManager().registerEvents(new MemberConfig(this), this);
		getServer().getPluginManager().registerEvents(new ArmorStandProtect(this), this);
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
	}
	
}
