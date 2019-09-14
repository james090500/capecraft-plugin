package net.capecraft;

import java.util.logging.Level;

import net.capecraft.commands.*;
import net.capecraft.events.comSpy.ComSpy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.capecraft.commands.rantp.RandomTP;
import net.capecraft.events.PlaytimeEventHandler;
import net.capecraft.events.ServerSlotManager;
import net.capecraft.events.afkcheck.AfkCheck;
import net.capecraft.events.afkcheck.IsAfkPlaceholder;
import net.capecraft.events.protect.ArmorStandProtect;
import net.capecraft.events.protect.ItemFrameProtect;

public class Main extends JavaPlugin {

	public static final String PREFIX = "§c§lCapeCraft §9»§r ";

	@Override
	public void onEnable() {

		//Wild
		getCommand("wild").setExecutor(new RandomTP());
		getCommand("rtp").setExecutor(new RandomTP());

		//Playtime
		getCommand("playtime").setExecutor(new MemberCommands(this));
		getCommand("playtimetop").setExecutor(new MemberCommands(this));
		getCommand("afk").setExecutor(new AfkCommand(this));

		//Cape
		getCommand("cape").setExecutor(new CapeCommand());
		
		//KeepInv Toggle
		getCommand("keepinv").setExecutor(new KeepInvToggle());

		//Command spy
		getCommand("comspy").setExecutor(new ComSpyCommand());
		
		//Events
		getServer().getPluginManager().registerEvents(new ServerSlotManager(), this);
		getServer().getPluginManager().registerEvents(new AfkCheck(this), this);
		getServer().getPluginManager().registerEvents(new PlaytimeEventHandler(this), this);
		getServer().getPluginManager().registerEvents(new ArmorStandProtect(this), this);
		getServer().getPluginManager().registerEvents(new ItemFrameProtect(this), this);
		getServer().getPluginManager().registerEvents(new ItemFrameProtect(this), this);
		getServer().getPluginManager().registerEvents(new ComSpy(), this);

		//PlaceHolderAPI
		if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
			new IsAfkPlaceholder(this).register();
		} else {
			getServer().getLogger().log(Level.SEVERE, "PlaceHolderAPI Not found. Placeholders will not working");
		}
	}
}