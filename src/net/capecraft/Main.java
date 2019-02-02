package net.capecraft;

import org.bukkit.plugin.java.JavaPlugin;

import net.capecraft.commands.PluginList;
import net.capecraft.joinleave.MessageListener;
import net.capecraft.member.MemberCommands;
import net.capecraft.member.MemberConfig;
import net.capecraft.protect.ArmorStandProtect;

public class Main extends JavaPlugin {

	public static final String PREFIX = "§c§lCapeCraft §8§l»§r "; 

	@Override
	public void onEnable() {

		//Wild
		getCommand("wild").setExecutor(new PluginList(this));
		getCommand("rtp").setExecutor(new PluginList(this));
		
		//Playtime
		getCommand("playtime").setExecutor(new MemberCommands(this));
		getCommand("playtimetop").setExecutor(new MemberCommands(this));
	
		//Firework
		getCommand("firework").setExecutor(new PluginList(this));
		
		//Events		
		getServer().getPluginManager().registerEvents(new MemberConfig(this), this);
		getServer().getPluginManager().registerEvents(new ArmorStandProtect(this), this);
		//getServer().getPluginManager().registerEvents(new DonatorJoin(this), this);

		//Message Channels
	    getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new MessageListener(this));
	}
	
	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		super.onDisable();
	}
	
}
