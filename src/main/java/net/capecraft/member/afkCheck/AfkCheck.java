package net.capecraft.member.afkCheck;

import net.capecraft.member.MemberConfig;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

public class AfkCheck implements Listener {

    Plugin plugin;

    public AfkCheck(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMoveEvent(PlayerMoveEvent event){
        MemberConfig memberconfig = new MemberConfig(plugin);
        Player p = event.getPlayer();
        String uuid = p.getUniqueId().toString();
        if(Boolean.parseBoolean(memberconfig.readConfig("isAfk", uuid).toString())){
            ComponentBuilder msg = new ComponentBuilder("Your AFK! ").color(ChatColor.RED).bold( true ).append("You don't gain playtime while AFK ").color(ChatColor.GREEN).bold( true ).append(":'( ").bold( true ).color(ChatColor.AQUA).append("Use ./afk to play normally!").color(ChatColor.GREEN).bold( true );
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(msg).create());
        }
    }


}
