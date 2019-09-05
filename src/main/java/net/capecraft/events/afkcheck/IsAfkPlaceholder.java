package net.capecraft.events.afkcheck;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.capecraft.events.PlaytimeEventHandler;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class IsAfkPlaceholder extends PlaceholderExpansion {

    private Plugin plugin;

    public IsAfkPlaceholder(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getIdentifier() {
        return "CapeCraft";
    }

    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    public String onPlaceholderRequest(Player player, String identifier) {
        if (identifier.equals("afk")) {
            PlaytimeEventHandler peh = new PlaytimeEventHandler(plugin);
            String uuid = player.getUniqueId().toString();
            if (Boolean.parseBoolean(peh.readConfig("isAfk", uuid).toString())) {
                return "AFK";
            }else{
                //You have to return something James https://i.imgur.com/uqDjCzo.png
                return "";
            }
        }
        return null;
    }
}

